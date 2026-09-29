package vn.iotstar.service;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
@Service
public class JwtService {
 private final byte[] key; private final long expiration; private final String issuer;
 public JwtService(@Value("${security.jwt.secret-key}") String secret,@Value("${security.jwt.expiration-time}") long expiration,@Value("${security.jwt.issuer}") String issuer){
  key=secret.getBytes(StandardCharsets.UTF_8);this.expiration=expiration;this.issuer=issuer;
  if(key.length<32 || expiration<=0)throw new IllegalArgumentException("JWT key must be >= 32 bytes and lifetime > 0");
 }
 public long getExpirationTime(){return expiration;}
 public String generateToken(UserDetails user){
  try {
   Instant now=Instant.now();
   JWTClaimsSet claims=new JWTClaimsSet.Builder().subject(user.getUsername()).issuer(issuer)
    .issueTime(Date.from(now)).expirationTime(Date.from(now.plusMillis(expiration))).jwtID(UUID.randomUUID().toString()).build();
   SignedJWT jwt=new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(),claims);
   jwt.sign(new MACSigner(key));return jwt.serialize();
  }catch(JOSEException e){throw new IllegalStateException("Cannot sign JWT",e);}
 }
 // Parse alone is NOT authentication: verify signature and claims first.
 public String extractUsername(String token){
  try {
   SignedJWT jwt=SignedJWT.parse(token);
   if(!JWSAlgorithm.HS256.equals(jwt.getHeader().getAlgorithm()) || !jwt.verify(new MACVerifier(key)))throw new Exception("Invalid signature");
   JWTClaimsSet c=jwt.getJWTClaimsSet(); Date now=new Date();
   if(!issuer.equals(c.getIssuer()) || c.getExpirationTime()==null || !c.getExpirationTime().after(now)
    || c.getIssueTime()==null || c.getIssueTime().after(now)
    || (c.getNotBeforeTime()!=null && c.getNotBeforeTime().after(now))
    || c.getSubject()==null || c.getSubject().isBlank())throw new Exception("Invalid claims");
   return c.getSubject();
  }catch(Exception e){throw new BadCredentialsException("JWT khong hop le hoac da het han",e);}
 }
}
