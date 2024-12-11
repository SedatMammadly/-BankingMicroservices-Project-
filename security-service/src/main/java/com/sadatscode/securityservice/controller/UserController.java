package com.sadatscode.securityservice.controller;

import com.sadatscode.securityservice.model.Users;
import com.sadatscode.securityservice.redis.RedisVerificationCodeService;
import com.sadatscode.securityservice.repository.UserRepository;
import com.sadatscode.securityservice.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RedisVerificationCodeService redisVerificationCodeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Users>> changePassword() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<String> getVerify(@RequestParam String email){
        return ResponseEntity.ok(redisVerificationCodeService.getVerificationCode(email));
    }

}