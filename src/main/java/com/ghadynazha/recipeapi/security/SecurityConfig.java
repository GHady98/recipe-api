package com.ghadynazha.recipeapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class for setting up authorization rules
 * and integrating the custom JWT authentication filter.
 *
 * Defines which endpoints are public, which require authentication,
 * and restricts admin-only access for certain routes.
 *
 * Author: Ghady Nazha
 */
@Profile("!test") // ✅ skip security if in test profile
@Configuration
@EnableWebSecurity


public class SecurityConfig {

    private final JwtFilter jwtFilter;

    /**
     * Constructs the security configuration with the required JWT filter.
     *
     * @param jwtFilter the custom filter used for JWT token validation
     */
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Defines the security filter chain used by Spring Security.
     * Configures endpoint access rules, adds the JWT filter, and disables CSRF protection.
     *
     * @param http the HttpSecurity object for configuring security rules
     * @return the configured SecurityFilterChain
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/all-users").hasAuthority("ADMIN") // ✅ only admins can view all users
                        .requestMatchers("/api/recipes/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}

