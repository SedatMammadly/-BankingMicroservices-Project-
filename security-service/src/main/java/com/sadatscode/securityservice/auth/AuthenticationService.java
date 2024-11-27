package com.sadatscode.securityservice.auth;

import com.sadatscode.securityservice.exception.ApplicationException;
import com.sadatscode.securityservice.exception.Exceptions;
import com.sadatscode.securityservice.jwt.JwtService;
import com.sadatscode.securityservice.model.Role;
import com.sadatscode.securityservice.model.Users;
import com.sadatscode.securityservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequest){
        Users user = repository.findByEmail(registerRequest.getEmail());
        if (user != null) {
            throw new ApplicationException(Exceptions.ExistingUserException);
        }
        Users save = authMapper.dtoToEntity(registerRequest,passwordEncoder);
        save.setRole(Role.USER);
        save.setCreatedAt(LocalDateTime.now());

        repository.save(save);
        return createAuthenticationTokens(save.getEmail());
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),authRequest.getPassword()));
        Users user = repository.findByEmail(authRequest.getEmail());
        if(user == null) {
            throw new ApplicationException(Exceptions.NotFoundException);
        }
        return createAuthenticationTokens(user.getEmail());
    }

    public AuthResponse createAuthenticationTokens(String email) {
        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);
        return AuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }


}
