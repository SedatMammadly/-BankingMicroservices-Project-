package com.sadatscode.securityservice.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisVerificationCodeService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String verificationCodeKey = "verificationCode:";
    @Value("${accessToken.expiredTime}")
    Long expiredTime;

    public void saveVerificationCode(String email,String verificationCode){
        String key = getVerificationKey(email);
        deleteVerificationCode(key);
        redisTemplate.opsForValue().set(key,verificationCode,expiredTime, TimeUnit.MILLISECONDS);
    }

    public void deleteVerificationCode(String key){
        redisTemplate.delete(key);
    }

    public String getVerificationCode(String email){
        String key = getVerificationKey(email);
        return redisTemplate.opsForValue().get(key);
    }

    private String getVerificationKey(String email) {
        return verificationCodeKey + email;
    }
}
