package com.ghadynazha.recipeapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration.
 *
 * – Permits public access to signup/login, Swagger UI, OpenAPI docs, the root “/”
 *   landing page, and all recipe‐read endpoints.
 * – Restricts `/api/auth/all-users` to ADMINs.
 * – All other endpoints require a valid JWT.
 *
 * Author: Ghady Nazha
 */
@Profile("!test")          // Skip this config when the “test” profile is active
@Configuration
@EnableWebSecurity


public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())                     // REST-style API → CSRF off

                .authorizeHttpRequests(auth -> auth
                        // ── PUBLIC ENDPOINTS ────────────────────────────────
                        .requestMatchers(
                                "/",                                   // Home / health message
                                "/swagger-ui/**",                      // Swagger UI
                                "/v3/api-docs/**",                     // OpenAPI spec
                                "/api/auth/signup",
                                "/api/auth/login",
                                "/api/recipes/**"                      // GET/search recipes
                        ).permitAll()

                        // ── ADMIN-ONLY ENDPOINTS ───────────────────────────
                        .requestMatchers("/api/auth/all-users").hasAuthority("ADMIN")

                        // ── EVERYTHING ELSE NEEDS AUTH ─────────────────────
                        .anyRequest().authenticated()
                )

                // Attach custom JWT filter before the username/password filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}