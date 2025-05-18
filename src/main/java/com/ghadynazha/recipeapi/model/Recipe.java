package com.ghadynazha.recipeapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Represents a recipe document stored in the "recipes" collection in MongoDB.
 * Contains basic fields such as title, ingredients, instructions, cooking time, and category.
 *
 * Used for CRUD operations via the Recipe API.
 *
 * Author: Ghady Nazha
 */
@Document(collection = "recipes")
@Data
@AllArgsConstructor
public class Recipe {
    /**
     * Default no-args constructor for Recipe.
     * Required by frameworks like Spring and Jackson.
     */
    public Recipe() {}

    /**
     * The unique integer ID of the recipe (auto-generated).
     */
    @Id
    private Integer id;

    /**
     * The title of the recipe.
     * Cannot be null or blank.
     */
    @NotBlank(message = "Title is required")
    private String title;

    /**
     * A list of ingredients required for the recipe.
     */
    private List<String> ingredients;

    /**
     * The instructions for preparing the recipe.
     * Cannot be null or blank.
     */
    @NotBlank(message = "Instructions are required")
    private String instructions;

    /**
     * The cooking time in minutes.
     */
    private int cookingTime;

    /**
     * The category or type of the recipe (e.g., Italian, Dessert).
     */
    private String category;
}