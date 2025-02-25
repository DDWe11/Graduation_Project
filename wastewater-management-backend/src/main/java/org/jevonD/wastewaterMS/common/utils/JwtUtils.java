package org.jevonD.wastewaterMS.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.TimeZone;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;  // 确保这个时间是以毫秒为单位的

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Beijing"));  // 设置默认时区为 CST
        this.secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        long now = System.currentTimeMillis(); // 获取当前 CST 时间的毫秒数
        Date nowDate = new Date(now);
        Date expirationDate = new Date(now + expirationTime);  // 计算过期时间

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(nowDate)  // 设置发行时间
                .setExpiration(expirationDate)  // 设置过期时间
                .signWith(secretKey, SignatureAlgorithm.HS256)  // 指定签名算法
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());  // 检查令牌是否已过期
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}