package com.sadatscode.securityservice.auth;

import com.sadatscode.securityservice.dto.VerifyRequest;
import com.sadatscode.securityservice.exception.ApplicationException;
import com.sadatscode.securityservice.exception.Exceptions;
import com.sadatscode.securityservice.jwt.JwtService;
import com.sadatscode.securityservice.model.Role;
import com.sadatscode.securityservice.model.Users;
import com.sadatscode.securityservice.redis.RedisTokenService;
import com.sadatscode.securityservice.redis.RedisVerificationCodeService;
import com.sadatscode.securityservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final RedisVerificationCodeService redisVerificationCodeService;
    private final UserRepository userRepository;
    @Value("${refreshToken.expiredTime}")
    private Long expiredTime;
    private final UserRepository repository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;

    public String register(RegisterRequest registerRequest) {
        Users user = repository.findByEmail(registerRequest.getEmail());
        if (user != null) {
            throw new ApplicationException(Exceptions.ExistingUserException);
        }
        user = authMapper.dtoToEntity(registerRequest, passwordEncoder);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        repository.save(user);
        return "Registered successfully";
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));
        Users user = repository.findByEmail(authRequest.getEmail());
        if (user == null) {
            throw new ApplicationException(Exceptions.NotFoundException);
        }
        userRepository.save(user);
        AuthResponse response = createAuthenticationTokens(user.getEmail());
        String email = redisTokenService.getRefreshToken(authRequest.getEmail());
        if (email != null) {
            redisTokenService.updateRefreshToken(response.getRefreshToken(), expiredTime, email);
        }
        redisTokenService.setRefreshToken(response.getRefreshToken(), expiredTime, user.getEmail());

        return response;
    }

    public AuthResponse createAuthenticationTokens(String email) {
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        return AuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    public AuthResponse refreshAuthToken(HttpServletRequest request) throws IOException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);
        String refreshToken;
        String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ApplicationException(Exceptions.InvalidTokenException);
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUserName(refreshToken);
        if (username != null) {
            System.out.println("refreshToken  +" + refreshToken);
            var userDetails = this.userDetailsService.loadUserByUsername(username);
            System.out.println(userDetails.getUsername() + "userdetail is not null");
            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtService.generateAccessToken(username);
                return AuthResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build();
            } else {
                throw new ApplicationException(Exceptions.InvalidTokenException);
            }
        } else {
            throw new ApplicationException(Exceptions.NotFoundException);
        }
    }

    public String logout(UserDetails userDetails) {
        Users user = repository.findByEmail(userDetails.getUsername());
        String userEmail = user.getEmail();
        redisTokenService.setBlockedRefreshToken(redisTokenService.getRefreshToken(userEmail),userEmail);
        System.out.println();
        redisTokenService.deleteRefreshToken(userEmail);
        return "Logged out successfully";
    }

    public Boolean verify(VerifyRequest request) {
        String verificationCode = redisVerificationCodeService.getVerificationCode(request.getEmail());
        return verificationCode.equals(request.getVerifyCode());
    }
}
