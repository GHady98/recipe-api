package com.ghadynazha.recipeapi.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Custom JWT filter that authenticates incoming requests by verifying the JWT token in the Authorization header.
 * Skips filtering for public endpoints like signup, login, Swagger, and public recipe APIs.
 *
 * This filter sets the user authentication in the Spring Security context if the token is valid.
 *
 * Author: Ghady Nazha
 */
@Component
public class JwtFilter extends OncePerRequestFilter{


    private final JwtUtil jwtUtil;

    /**
     * Constructor to inject JwtUtil.
     *
     * @param jwtUtil utility class for generating and validating JWT tokens
     */
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Defines which request paths should skip this filter (public endpoints).
     *
     * @param request the incoming HTTP request
     * @return true if the filter should be skipped, false otherwise
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/auth/signup")
                || path.equals("/api/auth/login")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/recipes"); // Public recipe endpoints
    }

    /**
     * Processes the incoming request and validates the JWT token.
     * If valid, extracts the username and role and sets the authentication context.
     *
     * @param request the incoming HTTP request
     * @param response the outgoing HTTP response
     * @param filterChain the filter chain to continue processing
     * @throws ServletException if the filter encounters a problem
     * @throws IOException if an I/O error occurs during processing
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtUtil.isTokenValid(token)) {
                    // ✅ Extract role from token
                    Claims claims = Jwts.parserBuilder().setSigningKey(jwtUtil.getKey()).build()
                            .parseClaimsJws(token)
                            .getBody();

                    String role = claims.get("role", String.class);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role)) // ✅ apply role
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}