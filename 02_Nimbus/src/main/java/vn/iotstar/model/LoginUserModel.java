package vn.iotstar.model;
import jakarta.validation.constraints.*;
public record LoginUserModel(@NotBlank @Email String email,@NotBlank @Size(max=72) String password) {}
