package com.sadatscode.securityservice.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "REFRESH_TOKEN:";
    private static final String BLOCKED_REFRESH_TOKEN_KEY_PREFIX = "BLOCKED_REFRESH_TOKEN:";

    public void setBlockedRefreshToken(String refreshToken, String email) {
        String key = BLOCKED_REFRESH_TOKEN_KEY_PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, 30, TimeUnit.DAYS);
    }

    public String getBlockedRefreshToken(String email) {
        String key = BLOCKED_REFRESH_TOKEN_KEY_PREFIX + email;
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean checkBlockedRefreshToken(String email, String refreshToken) {
        String token = getBlockedRefreshToken(email);
        return token != null && token.equals(refreshToken);
    }

    public void setRefreshToken(String refreshToken, Long expiredTime, String email) {
        String redisKey = getRefreshTokenKey(email);
        redisTemplate.opsForValue().set(redisKey, refreshToken, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String email) {
        String redisKey = getRefreshTokenKey(email);
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void updateRefreshToken(String refreshToken, Long expiredTime, String email) {
        String redisKey = getRefreshTokenKey(email);
        redisTemplate.opsForValue().getAndDelete(redisKey);
        setRefreshToken(refreshToken, expiredTime, email);
    }

    public void deleteRefreshToken(String email) {
        String redisKey = getRefreshTokenKey(email);
        redisTemplate.delete(redisKey);
    }


    private String getRefreshTokenKey(String email) {
        return REFRESH_TOKEN_KEY_PREFIX + email;
    }
}
