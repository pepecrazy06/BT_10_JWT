package vn.iotstar.service;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.model.UserResponse;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserService {
 private final UserRepository repository;
 public UserService(UserRepository r){repository=r;}
 public List<UserResponse> allUsers(){return repository.findAll().stream().map(UserResponse::from).toList();}
}
