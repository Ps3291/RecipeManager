package com.recipe.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "recipeentity")
public class RecipeEntity {

	@Id
	private Integer id;

	private String name;
	private String vegeterian;
	private String servings;
	private String ingredients;
	private String instructions;

}
