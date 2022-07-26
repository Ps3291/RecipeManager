package com.recipe.Utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.recipe.entity.RecipeEntity;
import com.recipe.model.Recipe;
import com.recipe.utils.RecipeConverter;

@SpringBootTest
public class RecipeConverterTest {

	/**
	 * Test case to check the conversion from Entity to Model
	 */
	@Test
	public void testEntityToModelConverter() {
		RecipeEntity recipeEntity = new RecipeEntity(10, "Soup", "No", "2",
				"1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");
		Recipe recipe = RecipeConverter.entityToModel(recipeEntity);
		assertThat(10).isEqualTo(recipe.getId());
		assertThat("Soup").isEqualTo(recipe.getName());
		assertThat("No").isEqualTo(recipe.getVegeterian());
		assertThat("2").isEqualTo(recipe.getServings());
		assertThat("Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.").isEqualTo(recipe.getInstructions());
		assertThat("1 piece chicken,  750ml water, salt, pepper").isEqualTo(recipe.getIngredients());
	}

	/**
	 * Test case to check the conversion from Model to Entity
	 */
	@Test
	public void testModelToEntityConverter() {
		Recipe recipe = new Recipe(10, "Soup", "No", "2", "1 piece chicken,  750ml water, salt, pepper",
				"Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.");
		RecipeEntity recipeEntity = RecipeConverter.modelToEntity(recipe);
		assertThat(10).isEqualTo(recipeEntity.getId());
		assertThat("Soup").isEqualTo(recipeEntity.getName());
		assertThat("No").isEqualTo(recipeEntity.getVegeterian());
		assertThat("2").isEqualTo(recipeEntity.getServings());
		assertThat("Cook chicken in boiled water for 15 minutes. Add salt and pepper as per taste.").isEqualTo(recipeEntity.getInstructions());
		assertThat("1 piece chicken,  750ml water, salt, pepper").isEqualTo(recipeEntity.getIngredients());

	}

}
