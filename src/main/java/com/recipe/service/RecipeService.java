package com.recipe.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipe.controller.RecipeController;
import com.recipe.entity.RecipeEntity;
import com.recipe.model.Recipe;
import com.recipe.repository.RecipeRepository;
import com.recipe.utils.RecipeConverter;

@Service
@Transactional
public class RecipeService {

	Logger logger = LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private RecipeRepository repo;

	/**
	 * This method fetches all the recipes from the DB
	 * 
	 * @return List of Recipe Model
	 */
	public List<Recipe> findAll() {
		final String methodName = "findAll";
		List<RecipeEntity> recipeEntities = repo.findAll();
		List<Recipe> recipes = new ArrayList<>(recipeEntities.size());
		recipeEntities.forEach(recipeEntity -> recipes.add(RecipeConverter.entityToModel(recipeEntity)));
		logger.info(methodName + ": Fetching all Recipes");
		return recipes;
	}

	/**
	 * This method creates a recipe in DB
	 * 
	 * @param recipe
	 * @return recipe model
	 */
	public Recipe save(Recipe recipe) {
		final String methodName = "save";
		logger.debug(methodName + ": Creating Recipe : {}", recipe);
		RecipeEntity recipeEntity = RecipeConverter.modelToEntity(recipe);

		if (repo.findById(recipeEntity.getId()).isEmpty()) {
			return RecipeConverter.entityToModel(repo.save(recipeEntity));
		} else {
			return null;
		}
	}

	/**
	 * This method updates an existing recipe in DB
	 * 
	 * @param recipe
	 * @return recipe model
	 */
	public Recipe update(Recipe recipe) {

		final String methodName = "update";
		RecipeEntity recipeEntity = RecipeConverter.modelToEntity(recipe);

		if (repo.findById(recipeEntity.getId()).isPresent()) {
			logger.info(methodName + ":" + "Updating Recipe");
			return RecipeConverter.entityToModel(repo.save(recipeEntity));
		} else {
			logger.info(methodName + ":" + "Recipe ID does not exist");
			return null;
		}
	}

	/**
	 * This method finds a recipe based on input id
	 * 
	 * @param id id of the recipe to search
	 * @return recipe model
	 */
	public Recipe findbyId(Integer id) {
		final String methodName = "findbyId";
		RecipeEntity recipeEntity;
		if (repo.findById(id).isPresent()) {
			recipeEntity = repo.findById(id).get();
			logger.info(methodName + " : Recipe found");
			return RecipeConverter.entityToModel(recipeEntity);
		} else {
			logger.info(methodName + " : Recipe with ID : {} not found", id);
			return null;
		}
	}

	/**
	 * This method deletes a recipe based on input id
	 * 
	 * @param id id of the recipe to be deleted
	 */
	public void delete(Integer id) {
		final String methodName = "delete";
		logger.info(methodName + " : Deleting recipe by Id : {}", id);
		repo.deleteById(id);
	}

	/**
	 * This method searches recipes based on input parameters and returns a list of
	 * filtered recipes
	 * 
	 * @param vegeterian   Value yes or no
	 * @param servings     number of servings
	 * @param ingredients  ingredients of the recipe
	 * @param include      should the above ingredients be included in search or
	 *                     excluded. Value True or False
	 * @param instructions instructions on how to prepare the recipe
	 * @return  List of Recipe Model
	 */
	public List<Recipe> searchRecipe(String vegeterian, String servings, String ingredients, Boolean include,
			String instructions) {
		final String methodName = "searchRecipe";

		List<RecipeEntity> recipeEntities;

		if (include) {
			logger.info(methodName
					+ " : Calling Include Method with params vegeterian {}, servings {}, ingredients {}, include {}, instructions {}",
					vegeterian, servings, ingredients, include, instructions);
			recipeEntities = repo.searchRecipeIncludeIngredient(vegeterian, servings, ingredients, instructions);
		} else {
			logger.info(methodName
					+ " : Calling Exclude Method with params vegeterian {}, servings {}, ingredients {}, include {}, instructions {}",
					vegeterian, servings, ingredients, include, instructions);
			recipeEntities = repo.searchRecipeExcludeIngredient(vegeterian, servings, ingredients, instructions);
		}

		if (recipeEntities.isEmpty()) {
			logger.info(methodName + " : No match found for the given search parameters");
			return null;
		} else {
			List<Recipe> recipes = new ArrayList<>(recipeEntities.size());
			recipeEntities.forEach(recipeEntity -> recipes.add(RecipeConverter.entityToModel(recipeEntity)));
			return recipes;
		}
	}
}
