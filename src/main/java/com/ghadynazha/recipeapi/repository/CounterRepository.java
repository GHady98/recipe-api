package com.ghadynazha.recipeapi.repository;
import com.ghadynazha.recipeapi.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for accessing and managing {@link Counter} documents in MongoDB.
 * Extends {@link MongoRepository} to provide CRUD operations for counters.
 *
 * Used to store and retrieve auto-increment sequence values (e.g., for user or recipe IDs).
 *
 * Author: Ghady Nazha
 */

public interface CounterRepository extends MongoRepository<Counter, String> {
}

