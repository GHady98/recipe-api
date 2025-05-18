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
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestSecurityConfig.class)
class RecipeApiApplicationTests {

	@LocalServerPort
	int port;

	@Autowired
	TestRestTemplate rest;

	private String url(String p) {
		return "http://localhost:" + port + p;
	}

	@Test
	void contextLoads() { }

	@Test
	void createAndReadRecipe() {
		Recipe r = new Recipe(null, "Pizza",
				List.of("Cheese", "Dough"), "Bake", 20, "Italian");

		Recipe created = rest.postForEntity(url("/api/recipes"), r, Recipe.class).getBody();
		assertThat(created).isNotNull();

		ResponseEntity<Recipe> get =
				rest.getForEntity(url("/api/recipes/" + created.getId()), Recipe.class);

		assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(get.getBody()).isNotNull();
		assertThat(get.getBody().getTitle()).isEqualTo("Pizza");
	}

	@Test
	void updateRecipe() {
		Recipe r = new Recipe(null, "Old", List.of("A"), "None", 5, "Fast");
		Recipe created = rest.postForEntity(url("/api/recipes"), r, Recipe.class).getBody();

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
	void deleteRecipe() {
		Recipe r = new Recipe(null, "ToDelete", List.of("X"), "None", 5, "None");
		Recipe created = rest.postForEntity(url("/api/recipes"), r, Recipe.class).getBody();

		rest.delete(url("/api/recipes/" + created.getId()));

		ResponseEntity<Recipe> resp =
				rest.getForEntity(url("/api/recipes/" + created.getId()), Recipe.class);

		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void listRecipes() {
		rest.postForEntity(url("/api/recipes"),
				new Recipe(null, "A", List.of("X"), "None", 5, "Type1"), Recipe.class);
		rest.postForEntity(url("/api/recipes"),
				new Recipe(null, "B", List.of("Y"), "None", 5, "Type2"), Recipe.class);

		Recipe[] recipes = rest.getForObject(url("/api/recipes"), Recipe[].class);
		assertThat(recipes).isNotNull();
		assertThat(recipes.length).isGreaterThanOrEqualTo(2);
	}
}