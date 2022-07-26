package com.recipe.Service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.model.Recipe;
import com.recipe.repository.RecipeRepository;
import com.recipe.service.RecipeService;

@SpringBootTest
class RecipeServiceTest {

	@Autowired
	RecipeService service;
	@Autowired
	RecipeRepository repo;

	/**
	 * Test save method of Service Class with correct data
	 */
	@Test
	public void testCreateRecipe() {
		Recipe recipe = new Recipe(10, "Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");

		service.save(recipe);
		assertThat(service.findbyId(recipe.getId())).isNotNull();
	}

	/**
	 * Test save method of Service Class with already existing ID(primary key)
	 */
	@Test
	public void testCreateRecipeNegative() {
		Recipe recipe = new Recipe(8, "Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");

		assertThat(service.save(recipe)).isNull();
	}

	/**
	 * Test findAll method of Service Class when data persisted in DB
	 */
	@Test
	public void testReadAllRecipes() {
		List<Recipe> recipes = service.findAll();
		assertThat(recipes).size().isGreaterThan(0);
	}

	/**
	 * Test findbyId(id) method of Service Class with id persisted in DB
	 */
	@Test
	public void testFetchSingleRecipe() {
		Recipe recipe = service.findbyId(4);
		assertThat("Yes").isEqualTo(recipe.getVegeterian());
	}

	/**
	 * Test findbyId(id) method of Service Class with id not persisted in DB
	 */
	@Test
	public void testFetchSingleRecipeNegative() {
		Recipe recipe = service.findbyId(14);
		assertThat(recipe).isNull();
	}

	/**
	 * Test update method of Service Class with recipe model and id existing in DB
	 */
	@Test
	public void testUpdateRecipe() {
		Recipe recipe = service.findbyId(2);
		recipe.setVegeterian("No");
		service.update(recipe);
		assertThat("Yes").isNotEqualTo(service.findbyId(2).getVegeterian());
	}

	/**
	 * Test delete method of Service Class with an id persisted in DB
	 */
	@Test
	public void testDeleteRecipe() {
		service.delete(8);
		assertThat(repo.existsById(8)).isFalse();
	}

	/**
	 * Test searchRecipe method of Service Class with an filter parameters
	 */
	@Test
	public void testSearchRecipe() {
		List<Recipe> recipe = service.searchRecipe("No", "1", "butter", true, "pan");
		Recipe recipe1 = new Recipe(6, "Chicken Steak", "No", "1",
				"4 chicken pieces, 2 Eggs (slightly whisked),1 tsp ginger garlic paste,1 cup onions, 2 tbsp coriander leaves, 2 green chillies,1 tsp black pepper powder,2 tbsp flour, 1 tbsp vinegar, salt, butter",
				"Flatten the chicken and mix with the other ingredients, except oil and marinate for 2 hours.Heat a non-stick pan with thin layer of oil.When hot, place chicken in it and cook till tender and brown on both sides.");
		Recipe recipe2 = new Recipe(9, "Omlette", "No", "1", "3 eggs beaten, 2 tsp butter, pinch of salt and pepper",
				"SSeason the beaten eggs with salt and pepper. Melt butter in a non-stick frying pan over a low heat. Pour the eggs into the pan and cook till golden brown.");
		assertThat(recipe1).usingRecursiveComparison().isEqualTo(recipe.get(0));
		assertThat(recipe2).usingRecursiveComparison().isEqualTo(recipe.get(1));
	}
}
