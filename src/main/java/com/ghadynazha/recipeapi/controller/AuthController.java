package com.ghadynazha.recipeapi.controller;
import com.ghadynazha.recipeapi.model.User;
import com.ghadynazha.recipeapi.repository.UserRepository;
import com.ghadynazha.recipeapi.security.JwtUtil;
import com.ghadynazha.recipeapi.service.CounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * REST controller that handles authentication and user management endpoints.
 * Provides signup, login, and user listing functionalities.
 *
 * Base URL: /api/auth
 *
 * Author: Ghady Nazha
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final CounterService counterService;

    /**
     * Constructs the AuthController with required dependencies.
     *
     * @param jwtUtil the utility class for generating JWT tokens
     * @param userRepo the repository for User persistence
     * @param counterService the service used for generating sequential user IDs
     */
    public AuthController(JwtUtil jwtUtil, UserRepository userRepo, CounterService counterService) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
        this.counterService = counterService;
    }

    /**
     * Registers a new user.
     * Automatically assigns "ADMIN" role if username is "admin@admin.com", otherwise defaults to "CLIENT".
     * Validates password strength and ensures username uniqueness.
     *
     * @param request a map containing "username", "password", and optionally "role"
     * @return a response with the registration result and generated user ID
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        String role;
        if ("admin@admin.com".equalsIgnoreCase(username)) {
            role = "ADMIN";
        } else {
            role = request.getOrDefault("role", "CLIENT").toUpperCase();
            if (!role.equals("ADMIN") && !role.equals("CLIENT")) {
                return ResponseEntity.badRequest().body("Role must be either 'ADMIN' or 'CLIENT'");
            }
        }

        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        if (!isStrongPassword(password)) {
            return ResponseEntity.badRequest().body("Password must include uppercase, lowercase, digit, and be 8+ characters.");
        }

        int sequence = counterService.getNextSequence("user");
        String generatedId = "2025" + String.format("%03d", sequence);

        User newUser = new User(generatedId, username, password, role);
        userRepo.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "User registered successfully",
                "userId", generatedId,
                "role", role
        ));
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param request a map containing "username" and "password"
     * @return a response containing the JWT token, user role, and user ID if login is successful
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body("Username and password are required");
        }

        Optional<User> user = userRepo.findByUsername(username);

        if (user.isPresent() && user.get().getPassword().equals(password)) {
            String role = user.get().getRole();
            String token = jwtUtil.generateToken(username, role);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "role", role,
                    "userId", user.get().getId()
            ));
        }

        return ResponseEntity.status(401).body("Invalid username or password");
    }

    /**
     * Returns a list of all registered users.
     * Accessible only by admin users.
     *
     * @return a list of users with ID, username, and role
     */
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(
                userRepo.findAll().stream().map(user -> Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "role", user.getRole()
                )).toList()
        );
    }

    /**
     * Validates password strength.
     * A strong password must contain at least one uppercase letter, one lowercase letter,
     * one digit, and be at least 8 characters long.
     *
     * @param password the password to validate
     * @return true if the password is strong, false otherwise
     */
    private boolean isStrongPassword(String password) {
        if (password.length() < 8) return false;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }
}