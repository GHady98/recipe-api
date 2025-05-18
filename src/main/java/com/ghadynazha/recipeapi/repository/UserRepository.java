package com.ghadynazha.recipeapi.repository;
import com.ghadynazha.recipeapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link User} documents in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations and custom queries.
 *
 * Author: Ghady Nazha
 */
public interface UserRepository extends MongoRepository<User, String> {
    /**
     * Finds a user by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by their ID.
     *
     * @param id the user ID to check
     * @return true if the user exists, false otherwise
     */
    boolean existsById(String id);
}

