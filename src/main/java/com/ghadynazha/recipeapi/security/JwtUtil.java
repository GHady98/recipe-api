package com.ghadynazha.recipeapi.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for creating and validating JWT tokens.
 * Uses a symmetric secret key and HMAC-SHA256 algorithm.
 *
 * Provides functionality to generate tokens, extract claims,
 * and validate the token structure and signature.
 *
 * Author: Ghady Nazha
 */
@Component
public class JwtUtil  {

    /**
     * Default constructor for JwtUtil.
     * Required by Spring Boot for component injection.
     */
    public JwtUtil() {}

    /**
     * Secret key used to sign and verify JWT tokens.
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Token expiration time in milliseconds (1 day).
     */
    private final long EXPIRATION_TIME = 86400000; // 1 day

    /**
     * Generates a JWT token with the given username and role.
     *
     * @param username the username to include in the token
     * @param role the user role to include as a custom claim
     * @return a signed JWT token string
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // ✅ Add role as a claim
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) stored in the token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Extracts the role claim from a given JWT token.
     *
     * @param token the JWT token
     * @return the role associated with the token
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().get("role", String.class);
    }

    /**
     * Validates the integrity and expiration of a JWT token.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Exposes the signing key for token verification (e.g., in JwtFilter).
     *
     * @return the secret signing key
     */
    public Key getKey() {
        return key;
    }
}