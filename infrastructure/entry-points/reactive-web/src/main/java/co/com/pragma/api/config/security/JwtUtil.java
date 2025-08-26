package co.com.pragma.api.config.security;

import co.com.pragma.model.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secretKey;

    public JwtUtil(@Value("${spring.security.oauth2.resourceserver.jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String user, String rol) {
        return Jwts.builder()
                .setSubject(user)
                .setIssuedAt(new Date())
                .claim("role", rol)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRol(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

}