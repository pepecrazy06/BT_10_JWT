package vn.iotstar.controller;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(AuthenticationException.class) ResponseEntity<?> auth(AuthenticationException e){return ResponseEntity.status(e instanceof AccountStatusException?403:401).body(Map.of("message","Email, mat khau sai hoac tai khoan khong kha dung"));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("message","Du lieu khong hop le: email dung dinh dang, ten 1-100 ky tu, mat khau 8-72 ky tu"));}
 @ExceptionHandler(DataIntegrityViolationException.class) ResponseEntity<?> duplicate(){return ResponseEntity.status(409).body(Map.of("message","Du lieu trung lap hoac khong hop le"));}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<?> status(ResponseStatusException e){return ResponseEntity.status(e.getStatusCode()).body(Map.of("message",e.getReason()==null?"Request failed":e.getReason()));}
}
