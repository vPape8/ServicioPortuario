package com.portuario.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Value("${security.jwt.secret:6E4B5F6A586E3272357538782F413F4428472B4B6250655368566B5970337336}")
    private String secretBase64;

    @Value("${security.jwt.expiration-minutes:60}")
    private long expirationMinutes;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
    }

    // CORRECCIÓN: Se eliminó el parámetro 'String role' y la línea .claim("role", role)
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(expirationMinutes));

        return Jwts.builder()
                .setSubject(username)
                // .claim("role", role) <--- ESTA LÍNEA CAUSABA EL ERROR
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    // Este método puede devolver null si no guardamos el rol, pero no romperá la compilación
    public String getRole(String token) {
        Object role = getAllClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}