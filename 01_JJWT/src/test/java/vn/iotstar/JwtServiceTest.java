package vn.iotstar;
import org.junit.jupiter.api.Test;
import vn.iotstar.service.JwtService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.BadCredentialsException;
import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest {
 private final String key="test-key-at-least-thirty-two-bytes-long-2026";
 @Test void signatureIssuerAndLifetime()throws Exception {
  var service=new JwtService(key,3600000,"demo");
  var user=User.withUsername("test@example.com").password("ignored").authorities("USER").build();
  String token=service.generateToken(user);
  assertEquals(user.getUsername(),service.extractUsername(token));
  assertThrows(BadCredentialsException.class,()->new JwtService(key+"other",3600000,"demo").extractUsername(token));
  assertThrows(BadCredentialsException.class,()->new JwtService(key,3600000,"other").extractUsername(token));
  String expired=new JwtService(key,1,"demo").generateToken(user);
  Thread.sleep(20);
  assertThrows(BadCredentialsException.class,()->service.extractUsername(expired));
 }
}
