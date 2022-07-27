package com.recipe.utils;

import com.recipe.entity.RecipeEntity;
import com.recipe.model.Recipe;

/**
 * This class has methods to convert model to entity and vice versa
 *
 */
public class RecipeConverter {

	/**
	 * Method responsible for Model to Entity conversion.
	 * 
	 * @param recipe as an input.
	 * @return RecipeEntity returned Entity.
	 */
	public static RecipeEntity modelToEntity(Recipe recipe) {
		return new RecipeEntity(recipe.getId(), recipe.getName(), recipe.getVegeterian(), recipe.getServings(),
				recipe.getIngredients(), recipe.getInstructions());
	}

	/**
	 * Method responsible for Entity to Model conversion.
	 * 
	 * @param recipeEntity as an input.
	 * @return Recipe returned Model.
	 */
	public static Recipe entityToModel(RecipeEntity recipeEntity) {
		return new Recipe(recipeEntity.getId(), recipeEntity.getName(), recipeEntity.getVegeterian(),
				recipeEntity.getServings(), recipeEntity.getIngredients(), recipeEntity.getInstructions());
	}

}
