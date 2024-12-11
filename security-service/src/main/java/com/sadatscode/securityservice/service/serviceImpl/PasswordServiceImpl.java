package com.sadatscode.securityservice.service.serviceImpl;

import com.sadatscode.securityservice.dto.ChangePasswordRequest;
import com.sadatscode.securityservice.dto.ResetPasswordRequest;
import com.sadatscode.securityservice.exception.ApplicationException;
import com.sadatscode.securityservice.exception.Exceptions;
import com.sadatscode.securityservice.model.Users;
import com.sadatscode.securityservice.redis.RedisVerificationCodeService;
import com.sadatscode.securityservice.repository.UserRepository;
import com.sadatscode.securityservice.service.PasswordService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final RedisVerificationCodeService redisVerificationCodeService;
    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void changePassword(UserDetails userDetails, ChangePasswordRequest changePasswordRequest) {
        Users user = userRepository.findByEmail(userDetails.getUsername());
        System.out.println(user.getEmail());
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ApplicationException(Exceptions.OldPassDontMatchException);
        }

        if (!Objects.equals(changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword())) {
            throw new ApplicationException(Exceptions.ConfirmPasswordDontMatchException);
        }

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(Exceptions.NewPasswordSameException);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @SneakyThrows
    public String forgetPassword(String email) {
        Users user = userRepository.findByEmail(email);
        String verificationCode = createVerificationCode();
        String text = "verification code: " + verificationCode;
        SimpleMailMessage message = new SimpleMailMessage();
        if (user != null) {
            message.setFrom(from);
            message.setSubject("Your gmail verification code");
            message.setText(text);
            message.setTo(email);
            mailSender.send(message);
            redisVerificationCodeService.saveVerificationCode(email,verificationCode);
        } else {
            throw new ApplicationException(Exceptions.NotFoundException);
        }
        return "Message send successfully";

    }

    public String createVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    @Override
    public void resetPassword(UserDetails userDetails, ResetPasswordRequest resetPasswordRequest) {
        Users user = userRepository.findByEmail(userDetails.getUsername());

        if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword())) {
            throw new ApplicationException(Exceptions.ConfirmPasswordDontMatchException);
        }

        if (passwordEncoder.matches(resetPasswordRequest.getNewPassword(), user.getPassword())) {
            throw new ApplicationException(Exceptions.NewPasswordSameException);
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

}
