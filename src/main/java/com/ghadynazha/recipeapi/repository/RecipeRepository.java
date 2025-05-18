package com.ghadynazha.recipeapi.repository;
import com.ghadynazha.recipeapi.model.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link Recipe} documents in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD and custom query methods.
 *
 * Author: Ghady Nazha
 */
public interface RecipeRepository extends MongoRepository<Recipe, Integer> {
    /**
     * Finds recipes whose titles contain the given substring (case-insensitive).
     *
     * @param title the title keyword to search for
     * @return list of matching Recipe objects
     */
    List<Recipe> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds recipes whose categories contain the given substring (case-insensitive).
     *
     * @param category the category keyword to search for
     * @return list of matching Recipe objects
     */
    List<Recipe> findByCategoryContainingIgnoreCase(String category);
}


