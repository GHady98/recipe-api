package com.ghadynazha.recipeapi;

import com.ghadynazha.recipeapi.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the Recipe API.
 * Verifies creation, retrieval, update, deletion, and listing of recipes using TestRestTemplate.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RecipeApiIntegrationTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    private String url(String p) {
        return "http://localhost:" + port + p;
    }

    @Test
    void testCreateAndGetRecipe() {
        Recipe req = new Recipe(null, "Pizza", List.of("Cheese", "Dough"), "Bake", 20, "Italian");

        Recipe created = rest.postForEntity(url("/api/recipes"), req, Recipe.class).getBody();
        assertThat(created).isNotNull();

        ResponseEntity<Recipe> resp =
                rest.getForEntity(url("/api/recipes/" + created.getId()), Recipe.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("Pizza");
    }

    @Test
    void testUpdateRecipe() {
        Recipe req = new Recipe(null, "Old", List.of("A"), "None", 5, "Fast");
        Recipe created = rest.postForEntity(url("/api/recipes"), req, Recipe.class).getBody();

        created.setTitle("New");

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Recipe> resp = rest.exchange(
                url("/api/recipes/" + created.getId()),
                HttpMethod.PUT,
                new HttpEntity<>(created, h),
                Recipe.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("New");
    }

    @Test
    void testDeleteRecipe() {
        Recipe req = new Recipe(null, "ToDelete", List.of("X"), "None", 5, "None");
        Recipe created = rest.postForEntity(url("/api/recipes"), req, Recipe.class).getBody();

        rest.delete(url("/api/recipes/" + created.getId()));

        ResponseEntity<Recipe> resp =
                rest.getForEntity(url("/api/recipes/" + created.getId()), Recipe.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testListRecipes() {
        rest.postForEntity(url("/api/recipes"),
                new Recipe(null, "A", List.of("X"), "None", 5, "Type1"), Recipe.class);
        rest.postForEntity(url("/api/recipes"),
                new Recipe(null, "B", List.of("Y"), "None", 5, "Type2"), Recipe.class);

        Recipe[] recipes = rest.getForObject(url("/api/recipes"), Recipe[].class);
        assertThat(recipes).isNotNull();
        assertThat(recipes.length).isGreaterThanOrEqualTo(2);
    }

}

