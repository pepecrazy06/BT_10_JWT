package vn.iotstar.service;
import vn.iotstar.entity.User;
import vn.iotstar.model.*;
import vn.iotstar.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Locale;
@Service
public class AuthenticationService {
 private final UserRepository users;private final PasswordEncoder encoder;private final AuthenticationManager manager;
 public AuthenticationService(UserRepository u,PasswordEncoder e,AuthenticationManager m){users=u;encoder=e;manager=m;}
 private String normalize(String email){return email.trim().toLowerCase(Locale.ROOT);}
 public User signup(RegisterUserModel r){
  if(r.password().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mat khau toi da 72 byte UTF-8");
  String email=normalize(r.email());
  if(users.findByEmail(email).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Email da ton tai");
  return users.saveAndFlush(new User(r.fullName().trim(),email,encoder.encode(r.password())));
 }
 public User authenticate(LoginUserModel r){return (User)manager.authenticate(new UsernamePasswordAuthenticationToken(normalize(r.email()),r.password())).getPrincipal();}
}
