package com.sadatscode.securityservice.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotBlank
    String userName;

    @Email(message = "Email should be email format")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @Size(min = 8, max = 16)
    @Pattern(regexp = "^(?!\\d+$).*$", message = "Password must contain different characters.")
    String password;

    @NotBlank(message = "Confirm password cannot be blank")
    String confirmPassword;

}
