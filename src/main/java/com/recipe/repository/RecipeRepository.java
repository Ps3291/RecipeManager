package com.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.recipe.entity.RecipeEntity;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

	@Query("SELECT r FROM RecipeEntity r WHERE (:vegeterian IS NULL OR r.vegeterian LIKE %:vegeterian%) AND (:servings IS NULL OR r.servings LIKE %:servings%) AND (:ingredients IS NULL OR r.ingredients LIKE %:ingredients%) AND (:instructions IS NULL OR r.instructions LIKE %:instructions%)")
	List<RecipeEntity> searchRecipeIncludeIngredient(String vegeterian, String servings, String ingredients,
			String instructions);

	@Query("SELECT r FROM RecipeEntity r WHERE (:vegeterian IS NULL OR r.vegeterian LIKE %:vegeterian%) AND (:servings IS NULL OR r.servings LIKE %:servings%) AND (:ingredients IS NULL OR r.ingredients NOT LIKE %:ingredients%) AND (:instructions IS NULL OR r.instructions LIKE %:instructions%)")
	List<RecipeEntity> searchRecipeExcludeIngredient(String vegeterian, String servings, String ingredients,
			String instructions);
}
