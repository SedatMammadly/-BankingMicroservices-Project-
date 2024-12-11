package com.sadatscode.securityservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${accessToken.expiredTime}")
    private Long accessTokenExpiredTime;
    @Value("${refreshToken.expiredTime}")
    private Long refreshTokenExpiredTime;
    @Value("${secret.private-key}")
    private String privateKey;
    @Value("${secret.public-key}")
    private String publicKey;

    // Rsa private key must be pkcs8 format//

    public String generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<String, Object>();
        return createToken(claims, email, accessTokenExpiredTime);
    }

    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<String, Object>();
        return createToken(claims, email, refreshTokenExpiredTime);
    }

    @SneakyThrows
    private String createToken(Map<String, Object> claims, String email, Long tokenExpiredTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiredTime))
                .signWith(generateJwtKeyEncryption(), SignatureAlgorithm.RS512)
                .compact();
    }

    private PrivateKey generateJwtKeyEncryption() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(privateKey.replaceAll("\\s+", ""));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }

    private PublicKey generateJwtKeyDecryption() throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.decodeBase64(publicKey.replaceAll("\\s+", ""));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public String extractUserName(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private Date extractTokenExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String jwtToken) {
        Date expiration = extractTokenExpiration(jwtToken);
        return expiration.before(new Date());
    }

    public Boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username = extractUserName(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    @SneakyThrows
    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(generateJwtKeyDecryption())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }
}
