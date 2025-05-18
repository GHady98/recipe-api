package com.ghadynazha.recipeapi;
import com.ghadynazha.recipeapi.model.Recipe;
import com.ghadynazha.recipeapi.model.RecipePage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Console-based CLI interface for interacting with the Recipe API.
 * Provides login, registration, recipe management, and user listing.
 *
 * Author: Ghady Nazha
 */
@Component
@Profile("!test")
public class    RecipeApiCLI  implements CommandLineRunner {
    private final RestTemplate restTemplate;
    private String token = null;
    private String role = null;

    /**
     * Constructs the CLI interface with a RestTemplate to interact with the Recipe API.
     *
     * @param restTemplate RestTemplate instance for making HTTP requests
     */
    public RecipeApiCLI(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... args) {
        String profile = System.getProperty("spring.profiles.active");
        if ("test".equalsIgnoreCase(profile)) return;

        Scanner scanner = new Scanner(System.in);
        int choice;

        printBanner();

        do {
            System.out.println("\n==================== MENU ====================");
            System.out.println("1. Signup (Register new user)");
            System.out.println("2. Login");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> signup(scanner);
                case 2 -> login(scanner);
                case 10 -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        } while (true);
    }

    private void printBanner() {
        System.out.println("\n\n");
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║             🍽️  WELCOME TO RECIPE API                ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ 🌐 Swagger UI: http://localhost:8080/swagger-ui      ║");
        System.out.println("║ 🔐 Use JWT token to access protected routes          ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ AUTHENTICATION ENDPOINTS:                            ║");
        System.out.println("║   ✅ POST    /api/auth/signup   → Register user      ║");
        System.out.println("║   ✅ POST    /api/auth/login    → Login user         ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ RECIPE ENDPOINTS:                                    ║");
        System.out.println("║   📄 GET     /api/recipes         → List recipes     ║");
        System.out.println("║   🔍 GET     /api/recipes/search  → Search recipes   ║");
        System.out.println("║   📦 GET     /api/recipes/paged   → Paginate list    ║");
        System.out.println("║   ➕ POST    /api/recipes         → Add recipe       ║");
        System.out.println("║   ✏️  PUT     /api/recipes/{id}    → Update recipe   ║");
        System.out.println("║   ❌ DELETE  /api/recipes/{id}    → Delete recipe    ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    private void signup(Scanner scanner) {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Map<String, String> payload = Map.of("username", username, "password", password);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/auth/signup",
                    payload,
                    String.class
            );
            System.out.println("✅ " + response.getBody());
        } catch (Exception e) {
            System.out.println("❌ Signup failed: " + e.getMessage());
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Map<String, String> payload = Map.of("username", username, "password", password);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/auth/login",
                    payload,
                    Map.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                token = (String) response.getBody().get("token");
                role = (String) response.getBody().get("role");

                System.out.println("🔓 Login successful!");
                System.out.println("👤 Role: " + role);
                if ("ADMIN".equalsIgnoreCase(role)) showAdminMenu(scanner);
                else showClientMenu(scanner);
            } else {
                System.out.println("❌ Login failed.");
            }
        } catch (Exception e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }
    }

    private void showAdminMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n====== 👑 ADMIN MENU ======");
            System.out.println("1. List All Recipes");
            System.out.println("2. Add Recipe");
            System.out.println("3. Delete Recipe");
            System.out.println("4. Search Recipe");
            System.out.println("5. Update Recipe");
            System.out.println("6. Paginate Recipes");
            System.out.println("7. View All Users");
            System.out.println("8. Logout");

            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> listRecipes();
                case 2 -> addRecipe(scanner);
                case 3 -> deleteRecipeById(scanner);
                case 4 -> searchRecipeByTitle(scanner);
                case 5 -> updateRecipe(scanner);
                case 6 -> paginateRecipes(scanner);
                case 7 -> viewAllUsers();
                case 8 -> {
                    token = null;
                    role = null;
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        } while (true);
    }

    private void showClientMenu(Scanner scanner) {
        int choice;
        do {
            System.out.println("\n====== 👤 CLIENT MENU ======");
            System.out.println("1. List All Recipes");
            System.out.println("2. Search Recipe");
            System.out.println("3. Logout");

            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("❌ Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> listRecipes();
                case 2 -> searchRecipeByTitle(scanner);
                case 3 -> {
                    token = null;
                    role = null;
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        } while (true);
    }

    private void listRecipes() {
        try {
            ResponseEntity<List<Recipe>> response = restTemplate.exchange(
                    "http://localhost:8080/api/recipes",
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),
                    new ParameterizedTypeReference<>() {}
            );
            List<Recipe> recipes = response.getBody();
            if (recipes == null || recipes.isEmpty()) {
                System.out.println("📭 No recipes found.");
            } else {
                recipes.forEach(this::printRecipe);
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch recipes: " + e.getMessage());
        }
    }

    private void addRecipe(Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        System.out.print("Enter ingredients (comma-separated): ");
        List<String> ingredients = Arrays.asList(scanner.nextLine().split(","));
        System.out.print("Enter instructions: ");
        String instructions = scanner.nextLine();
        System.out.print("Enter cooking time (minutes): ");
        int time = Integer.parseInt(scanner.nextLine());

        Recipe recipe = new Recipe(null, title, ingredients, instructions, time, category);
        try {
            ResponseEntity<Recipe> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/recipes",
                    new HttpEntity<>(recipe, authHeaders()),
                    Recipe.class
            );
            System.out.println("✅ Recipe added: " + response.getBody().getTitle());
        } catch (Exception e) {
            System.out.println("❌ Failed to add recipe: " + e.getMessage());
        }
    }

    private void deleteRecipeById(Scanner scanner) {
        System.out.print("Enter recipe ID: ");
        String id = scanner.nextLine();
        try {
            restTemplate.exchange(
                    "http://localhost:8080/api/recipes/" + id,
                    HttpMethod.DELETE,
                    new HttpEntity<>(authHeaders()),
                    Void.class
            );
            System.out.println("🗑️ Recipe deleted.");
        } catch (Exception e) {
            System.out.println("❌ Failed to delete: " + e.getMessage());
        }
    }

    private void searchRecipeByTitle(Scanner scanner) {
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine();
        try {
            ResponseEntity<List<Recipe>> response = restTemplate.exchange(
                    "http://localhost:8080/api/recipes/search?title=" + title,
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),
                    new ParameterizedTypeReference<>() {}
            );
            List<Recipe> results = response.getBody();
            if (results == null || results.isEmpty()) {
                System.out.println("❌ No recipes found.");
            } else {
                results.forEach(this::printRecipe);
            }
        } catch (Exception e) {
            System.out.println("❌ Search failed: " + e.getMessage());
        }
    }

    private void updateRecipe(Scanner scanner) {
        System.out.print("Enter recipe ID to update: ");
        String id = scanner.nextLine();
        try {
            ResponseEntity<Recipe> response = restTemplate.exchange(
                    "http://localhost:8080/api/recipes/" + id,
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),
                    Recipe.class
            );
            Recipe existing = response.getBody();
            if (existing == null) {
                System.out.println("❌ Recipe not found.");
                return;
            }

            System.out.print("New title [" + existing.getTitle() + "]: ");
            String title = scanner.nextLine();
            if (!title.isBlank()) existing.setTitle(title);

            System.out.print("New category [" + existing.getCategory() + "]: ");
            String category = scanner.nextLine();
            if (!category.isBlank()) existing.setCategory(category);

            System.out.print("New cooking time [" + existing.getCookingTime() + "]: ");
            String time = scanner.nextLine();
            if (!time.isBlank()) existing.setCookingTime(Integer.parseInt(time));

            System.out.print("New ingredients (comma-separated): ");
            String ing = scanner.nextLine();
            if (!ing.isBlank()) existing.setIngredients(Arrays.asList(ing.split(",")));

            System.out.print("New instructions [" + existing.getInstructions() + "]: ");
            String instructions = scanner.nextLine();
            if (!instructions.isBlank()) existing.setInstructions(instructions);

            restTemplate.exchange(
                    "http://localhost:8080/api/recipes/" + id,
                    HttpMethod.PUT,
                    new HttpEntity<>(existing, authHeaders()),
                    Recipe.class
            );
            System.out.println("✅ Recipe updated.");
        } catch (Exception e) {
            System.out.println("❌ Update failed: " + e.getMessage());
        }
    }

    private void paginateRecipes(Scanner scanner) {
        System.out.print("Enter page number: ");
        int page = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter page size: ");
        int size = Integer.parseInt(scanner.nextLine());
        try {
            ResponseEntity<RecipePage> response = restTemplate.exchange(
                    "http://localhost:8080/api/recipes/paged?page=" + page + "&size=" + size,
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),
                    new ParameterizedTypeReference<>() {}
            );
            List<Recipe> results = response.getBody().getContent();
            if (results == null || results.isEmpty()) {
                System.out.println("📭 No recipes found in this page.");
            } else {
                results.forEach(this::printRecipe);
            }
        } catch (Exception e) {
            System.out.println("❌ Pagination failed: " + e.getMessage());
        }
    }

    private void viewAllUsers() {
        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "http://localhost:8080/api/auth/all-users",
                    HttpMethod.GET,
                    new HttpEntity<>(authHeaders()),
                    new ParameterizedTypeReference<>() {}
            );
            List<Map<String, Object>> users = response.getBody();
            if (users == null || users.isEmpty()) {
                System.out.println("📭 No users found.");
            } else {
                users.forEach(user -> {
                    System.out.println("🆔 ID: " + user.get("id"));
                    System.out.println("👤 Username: " + user.get("username"));
                    System.out.println("🔐 Role: " + user.get("role"));
                    System.out.println("----------------------------------");
                });
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch users: " + e.getMessage());
        }
    }

    private void printRecipe(Recipe r) {
        System.out.println("🍲 Title: " + r.getTitle());
        System.out.println("📚 Category: " + r.getCategory());
        System.out.println("🧂 Ingredients: " + r.getIngredients());
        System.out.println("📖 Instructions: " + r.getInstructions());
        System.out.println("⏱️ Cooking Time: " + r.getCookingTime() + " minutes");
        System.out.println("------------------------------------------------");
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (token != null) headers.setBearerAuth(token);
        return headers;
    }
}