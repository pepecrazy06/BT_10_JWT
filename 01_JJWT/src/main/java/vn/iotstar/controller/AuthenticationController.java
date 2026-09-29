package vn.iotstar.controller;
import vn.iotstar.model.*;
import vn.iotstar.service.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/auth")
public class AuthenticationController {
 private final AuthenticationService auth;private final JwtService jwt;
 public AuthenticationController(AuthenticationService a,JwtService j){auth=a;jwt=j;}
 @PostMapping("/signup") public UserResponse register(@Valid @RequestBody RegisterUserModel r){return UserResponse.from(auth.signup(r));}
 @PostMapping("/login") public LoginResponse login(@Valid @RequestBody LoginUserModel r){return new LoginResponse(jwt.generateToken(auth.authenticate(r)),jwt.getExpirationTime());}
}
