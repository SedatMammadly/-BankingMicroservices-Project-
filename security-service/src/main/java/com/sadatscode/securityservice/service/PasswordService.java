package com.sadatscode.securityservice.service;

import com.sadatscode.securityservice.dto.ChangePasswordRequest;
import com.sadatscode.securityservice.dto.ResetPasswordRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface PasswordService {
    void changePassword(UserDetails userDetails, ChangePasswordRequest changePasswordRequest);
    String forgetPassword(String email);
    void resetPassword(UserDetails userDetails, ResetPasswordRequest resetPasswordRequest);
}
