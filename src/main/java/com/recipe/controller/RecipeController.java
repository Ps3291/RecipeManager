package com.recipe.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recipe.model.Recipe;
import com.recipe.service.RecipeService;


@RestController
public class RecipeController {

	Logger logger = LoggerFactory.getLogger(RecipeController.class);

	@Autowired
	private RecipeService service;

	/**
	 * EndPoint to fetch all available recipes in DB
	 * 
	 * @return : list of all recipes
	 */
	@GetMapping("/recipe")
	public ResponseEntity<List<Recipe>> fetchAllRecipes() {
		final String methodName = "fetchAllRecipes";
		logger.info(methodName + " : Call service to fetch all the available recipes.");
		List<Recipe> recipeList = service.findAll();
		if (recipeList == null) {
			logger.info(methodName + " : No recipes available in DB.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(recipeList);
		}
	}

	/**
	 * EndPoint to fetch a recipe by id passed in path parameter
	 * 
	 * @param id
	 * @return : Recipe with requested id
	 */
	@GetMapping("/recipe/{id}")
	public ResponseEntity<Recipe> getRecipeById(@PathVariable Integer id) {
		final String methodName = "getRecipeById";
		logger.info(methodName + " : Call service to fetch recipe by Id : " + id);
		Recipe recipe = service.findbyId(id);
		if (recipe == null) {
			logger.info(methodName + " : No recipe exists by Id : " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(recipe);
		}
	}

	/**
	 * EndPoint to create a new recipe and store in DB
	 * 
	 * @param recipe
	 * @return : String status message of the result
	 */
	@PostMapping("/recipe/create")
	public ResponseEntity<String> createRecipeById(@RequestBody Recipe recipe) {
		final String methodName = "createRecipeById";
		Recipe recipeObject = service.save(recipe);

		if (recipeObject == null) {
			logger.info(methodName + " : Could not create recipe by ID : " + recipe.getId());
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Error creating recipe because Id : " + recipe.getId() + " already exists");
		} else {
			logger.info(methodName + " : Call service to create recipe by Id : " + recipe.getId());
			return ResponseEntity.status(HttpStatus.CREATED).body("Recipe created successfully with Id :" + recipe.getId());
		}
	}

	/**
	 * EndPoint to update a recipe by id passed in path parameter
	 * 
	 * @param recipe
	 * @param id
	 * @return : String message status of result
	 */
	@PutMapping("/recipe/update")
	public ResponseEntity<String> updateRecipeById(@RequestBody Recipe recipe) {
		final String methodName = "updateRecipeById";
		if (service.findbyId(recipe.getId()) != null) {
			logger.info(methodName + " : Call service to update recipe by Id : {}", recipe.getId());
			service.update(recipe);
			return ResponseEntity.status(HttpStatus.OK).body("Updated recipe with ID: " + recipe.getId());
		} else {
			logger.info(methodName + " : Recipe to update does not exist, Id : " + recipe.getId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Recipe ID to update : " + recipe.getId() + " does not exist");
		}
	}

	/**
	 * EndPoint to delete a recipe by id passed in path parameter
	 * 
	 * @param id
	 * @return Response Entity String message
	 */
	@DeleteMapping("/recipe/delete/{id}")
	public ResponseEntity<String> deleteRecipe(@PathVariable Integer id) {
		final String methodName = "deleteRecipe";
		try {
			logger.info(methodName + " : Call service to delete recipe by Id : " + id);
			service.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body("Deleted Recipe with ID : " + id);
		} catch (EmptyResultDataAccessException e) {
			logger.info(methodName + " : Recipe to delete does not exist, Id : " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Recipe found with ID : " + id);
		}
	}

	/**
	 * EndPoint to search a recipe by parameters
	 * 
	 * @param vegeterian
	 * @param servings
	 * @param ingredients
	 * @param include
	 * @param instructions
	 * @return : List of recipe model based on search filters in input parameters
	 */
	@GetMapping("/recipe/search")
	public ResponseEntity<List<Recipe>> searchRecipe(@RequestParam("vegeterian") String vegeterian,
			@RequestParam("servings") String servings, @RequestParam("ingredients") String ingredients,
			@RequestParam(name = "include", defaultValue = "true") Boolean include,
			@RequestParam("instructions") String instructions) {
		final String methodName = "searchRecipe";

		logger.info(methodName + " : Calling service to search recipes by filter");
		List<Recipe> recipeList = service.searchRecipe(vegeterian, servings, ingredients, include, instructions);
		if (recipeList == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(recipeList);
		}
	}
}
