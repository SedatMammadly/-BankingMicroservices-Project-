package com.sadatscode.securityservice.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {
    @Email(message = "Email should be email format")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?!\\d+$).*$", message = "Password must contain different characters.")
    String password;
}
