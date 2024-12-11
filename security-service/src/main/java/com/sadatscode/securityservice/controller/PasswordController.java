package com.sadatscode.securityservice.controller;
import com.sadatscode.securityservice.dto.ChangePasswordRequest;
import com.sadatscode.securityservice.dto.ResetPasswordRequest;
import com.sadatscode.securityservice.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService service;

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserDetails userDetails,@RequestBody ChangePasswordRequest changePasswordRequest) {
        service.changePassword(userDetails, changePasswordRequest);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<String> forgetPassword(@RequestParam String email){
        return ResponseEntity.ok(service.forgetPassword(email));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@AuthenticationPrincipal UserDetails userDetails,@RequestBody ResetPasswordRequest resetPasswordRequest){
        service.resetPassword(userDetails,resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }
}
