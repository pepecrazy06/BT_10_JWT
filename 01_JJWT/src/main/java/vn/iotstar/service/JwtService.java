package vn.iotstar.service;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;
@Service
public class JwtService {
 private final SecretKey key;private final long expiration;private final String issuer;
 public JwtService(@Value("${security.jwt.secret-key}") String secret,@Value("${security.jwt.expiration-time}") long expiration,@Value("${security.jwt.issuer}") String issuer){
 key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.expiration=expiration;this.issuer=issuer;
 if(expiration<=0)throw new IllegalArgumentException("Lifetime must be positive");}
 public long getExpirationTime(){return expiration;}
 public String generateToken(UserDetails user){Instant now=Instant.now();return Jwts.builder().subject(user.getUsername()).issuer(issuer).issuedAt(Date.from(now)).expiration(Date.from(now.plusMillis(expiration))).id(UUID.randomUUID().toString()).signWith(key,Jwts.SIG.HS256).compact();}
 public String extractUsername(String token){try{
 Jws<Claims> parsed=Jwts.parser().verifyWith(key).requireIssuer(issuer).build().parseSignedClaims(token);
 Claims c=parsed.getPayload();Date now=new Date();
 if(!"HS256".equals(parsed.getHeader().getAlgorithm()) || c.getExpiration()==null || !c.getExpiration().after(now) || c.getIssuedAt()==null || c.getIssuedAt().after(now) || c.getSubject()==null || c.getSubject().isBlank())throw new IllegalArgumentException("Invalid claims");
 return c.getSubject();
 }catch(Exception e){throw new BadCredentialsException("JWT khong hop le hoac da het han",e);}}
}
