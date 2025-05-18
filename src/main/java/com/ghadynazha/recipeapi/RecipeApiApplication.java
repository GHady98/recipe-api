package com.ghadynazha.recipeapi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Main entry point for the Recipe API Spring Boot Application.
 * Initializes and runs the Spring Boot context.
 * <p>
 * Author: Ghady Nazha
 */
@SpringBootApplication
public class RecipeApiApplication {

    /**
     * Default constructor for Spring Boot initialization.
     */
    public RecipeApiApplication() {
        // Default constructor
    }

    @Autowired
    private Environment env;

    /**
     * Starts the Spring Boot application.
     *
     * @param args CLI arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(RecipeApiApplication.class, args);
    }

    /**
     * Registers a Spring-managed RestTemplate bean for making HTTP requests.
     *
     * @return RestTemplate instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Prints a console banner after app startup — skips during test profile.
     */
    @PostConstruct
    public void printBanner() {
        String profile = env.getProperty("spring.profiles.active", "");
        if ("test".equalsIgnoreCase(profile)) return;

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
}
