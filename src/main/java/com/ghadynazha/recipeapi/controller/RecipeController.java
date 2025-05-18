package com.ghadynazha.recipeapi.controller;
import com.ghadynazha.recipeapi.model.Recipe;
import com.ghadynazha.recipeapi.repository.RecipeRepository;
import com.ghadynazha.recipeapi.service.CounterService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * REST controller for handling CRUD operations on Recipe entities.
 * Provides endpoints for creating, retrieving, updating, deleting,
 * paginating, and searching recipes.
 *
 * Base URL: /api/recipes
 *
 * Author: Ghady Nazha
 */
@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeRepository recipeRepo;
    private final CounterService counterService;

    /**
     * Constructs a new RecipeController with required dependencies.
     *
     * @param recipeRepo the Recipe repository
     * @param counterService the service used for generating auto-incremented IDs
     */
    public RecipeController(RecipeRepository recipeRepo, CounterService counterService) {
        this.recipeRepo = recipeRepo;
        this.counterService = counterService;
    }

    /**
     * Creates a new Recipe with an auto-incremented ID.
     *
     * @param recipe the Recipe object to be created
     * @return the created Recipe in the response
     */
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@Valid @RequestBody Recipe recipe) {
        recipe.setId(counterService.getNextSequence("recipe"));
        return ResponseEntity.ok(recipeRepo.save(recipe));
    }

    /**
     * Retrieves a list of all recipes.
     *
     * @return a list of all Recipe objects
     */
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeRepo.findAll();
    }

    /**
     * Retrieves a paginated list of recipes sorted by a specific field.
     *
     * @param page the page number (0-based)
     * @param size the number of items per page
     * @param sortBy the field to sort by (default is "title")
     * @return a Page object containing recipes
     */
    @GetMapping("/paged")
    public Page<Recipe> getPagedRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return recipeRepo.findAll(pageable);
    }

    /**
     * Retrieves a single recipe by its ID.
     *
     * @param id the ID of the recipe
     * @return the Recipe object if found, otherwise 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Integer id) {
        return recipeRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing recipe by ID.
     *
     * @param id the ID of the recipe to update
     * @param updated the updated Recipe object
     * @return the updated Recipe if found, otherwise 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Integer id, @Valid @RequestBody Recipe updated) {
        return recipeRepo.findById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(recipeRepo.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a recipe by its ID.
     *
     * @param id the ID of the recipe to delete
     * @return HTTP 204 No Content if deleted, 404 Not Found otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Integer id) {
        return recipeRepo.findById(id)
                .map(recipe -> {
                    recipeRepo.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Searches for recipes by title or category.
     *
     * @param title optional title substring to search for
     * @param category optional category substring to search for
     * @return list of matching Recipe objects, or all recipes if no filters are provided
     */
    @GetMapping("/search")
    public List<Recipe> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category
    ) {
        if (title != null) {
            return recipeRepo.findByTitleContainingIgnoreCase(title);
        } else if (category != null) {
            return recipeRepo.findByCategoryContainingIgnoreCase(category);
        }
        return recipeRepo.findAll();
    }
}