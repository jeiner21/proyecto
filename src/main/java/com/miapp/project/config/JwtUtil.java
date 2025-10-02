package com.miapp.project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Clave secreta para firmar los tokens
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tiempo de expiración del token (ejemplo: 1 hora = 3600000 ms)
    private static final long EXPIRATION_TIME = 3600000;

    // 🔹 Generar un token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // El "dueño" del token
                .setIssuedAt(new Date()) // Fecha de creación
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiración
                .signWith(SECRET_KEY) // Firma con la clave secreta
                .compact();
    }

    // 🔹 Obtener el usuario dentro del token
    public static String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // 🔹 Validar token
    public static boolean validateToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    // 🔹 Ver si expiró
    private static boolean isTokenExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().before(new Date());
    }
}
