package com.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
	private Integer id;
	private String name;
	private String vegeterian;
	private String servings;
	private String ingredients;
	private String instructions;
}
