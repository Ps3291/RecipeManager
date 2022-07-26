package com.recipe.Controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipe.model.Recipe;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecipeControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	/**
	 * Integration Test to fetch all recipes
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(1)
	public void testIntegrationFetchAllRecipes() throws Exception {
		mvc.perform(get("/recipe").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is("Maggie Noodles")))
				.andExpect(jsonPath("$[1].vegeterian", is("Yes"))).andExpect(jsonPath("$[2].servings", is("2")))
				.andExpect(jsonPath("$[3].id", is(4))).andExpect(jsonPath("$[4].name", is("Banana Bread")))
				.andExpect(jsonPath("$[5].vegeterian", is("No"))).andExpect(jsonPath("$.length()", is(9)));
	}

	/**
	 * Integration test to get a recipe by ID
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(2)
	public void testIntegrationFetchRecipeById() throws Exception {
		mvc.perform(get("/recipe/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("Coffee")));
	}

	/**
	 * Integration Test to save a recipe.
	 * 
	 * @throws Exception exception if any.
	 */
	@Test
	@Order(3)
	public void testIntegrationSaveRecipe() throws Exception {
		Recipe recipe = new Recipe(10, "Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");
		mvc.perform(post("/recipe/create").content(new ObjectMapper().writeValueAsString(recipe))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		mvc.perform(get("/recipe").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[9].name", is("Soup")));
	}

	/**
	 * Integration Test to save when already existing recipe id.
	 * 
	 * @throws Exception possible exception if any.
	 */
	@Test
	@Order(4)
	public void testIntegrationSaveRecipeWhenIdPresent() throws Exception {
		Recipe recipe = new Recipe(8, "Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");
		mvc.perform(post("/recipe/create").content(new ObjectMapper().writeValueAsString(recipe))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
	}

	/**
	 * Integration Test to update a recipe
	 * 
	 * @throws Exception possible exception if any.
	 */
	@Test
	@Order(5)
	public void testIntegrationUpdateRecipe() throws Exception {

		Recipe recipe = new Recipe(8, "Chicken Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");

		mvc.perform(put("/recipe/update").content(new ObjectMapper().writeValueAsString(recipe))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(get("/recipe/8").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("name", is("Chicken Soup")));
	}

	/**
	 * Integration Test to update a recipe when the given id does not exist
	 * 
	 * @throws Exception possible exception if any.
	 */
	@Test
	@Order(6)
	public void testIntegrationUpdateRecipeWhenIdNotPresent() throws Exception {

		Recipe recipe = new Recipe(11, "Chicken Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");

		mvc.perform(put("/recipe/update").content(new ObjectMapper().writeValueAsString(recipe))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

	}

	/**
	 * Integration test to search recipes based on parameters
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(7)
	public void testIntegrationSearchRecipeByParams() throws Exception {
		mvc.perform(get("/recipe/search?vegeterian=No&servings=1&ingredients=butter&include=true&instructions=pan")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name", is("Chicken Steak"))).andExpect(jsonPath("$[0].servings", is("1")))
				.andExpect(jsonPath("$[1].name", is("Omlette"))).andExpect(jsonPath("$[1].vegeterian", is("No")));
	}

	/**
	 * Integration test to search recipes based on parameters when there is no
	 * available recipe based on the filters
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(8)
	public void testIntegrationSearchRecipeByParamsNoContent() throws Exception {
		mvc.perform(get("/recipe/search?vegeterian=No&servings=10&ingredients=butter&include=true&instructions=pan")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	/**
	 * Integration test to search recipes based on malformed parameters
	 * 
	 * @throws Exception
	 */
	@Test
	@Order(9)
	public void testIntegrationSearchRecipeByInvalidParams() throws Exception {
		mvc.perform(get("/recipe/search?vegeterian=No&servings=10&ingredients=butter&include=true")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	/**
	 * Integration Test to delete a particular recipe by id
	 * 
	 * @throws Exception possible exception if any.
	 */
	@Test
	@Order(10)
	public void testIntegrationDeleteRecipeById() throws Exception {
		mvc.perform(delete("/recipe/delete/6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		mvc.perform(get("/recipe/6").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	/**
	 * Integration Test to delete a particular recipe by id when id not present
	 * 
	 * @throws Exception possible exception if any.
	 */
	@Test
	@Order(11)
	public void testIntegrationDeleteRecipeByIdInvalidId() throws Exception {
		mvc.perform(delete("/recipe/delete/12").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
