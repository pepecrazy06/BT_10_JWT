package vn.iotstar.controller;
import vn.iotstar.entity.User;
import vn.iotstar.model.UserResponse;
import vn.iotstar.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/users")
public class UserController {
 private final UserService users;public UserController(UserService u){users=u;}
 @GetMapping("/me") public UserResponse me(@AuthenticationPrincipal User user){return UserResponse.from(user);}
 @GetMapping({"","/"}) public List<UserResponse> all(){return users.allUsers();}
}
