package com.ghadynazha.recipeapi.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a user document stored in the "users" collection in MongoDB.
 * Contains basic fields for authentication and authorization purposes.
 *
 * Used during signup, login, and user listing in the Recipe API.
 *
 * Author: Ghady Nazha
 */
@Document(collection = "users")
@Data
@AllArgsConstructor
public class User {
    /**
     * Default constructor for User.
     * Required by frameworks like Spring and Jackson.
     */
    public User() {}

    /**
     * Unique ID assigned to the user (e.g., "2025001").
     */
    @Id
    private String id;

    /**
     * Username used for login. Must be unique.
     */
    private String username;

    /**
     * Password associated with the user account.
     */
    private String password;

    /**
     * Role of the user. Either "ADMIN" or "CLIENT".
     */
    private String role;
}

