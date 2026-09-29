package vn.iotstar.model;
import jakarta.validation.constraints.*;
public record RegisterUserModel(@NotBlank @Email @Size(max=100) String email,
 @NotBlank @Size(min=8,max=72) String password, @NotBlank @Size(max=100) String fullName) {}
