package com.recipe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import com.recipe.controller.RecipeController;

@SpringBootTest
public class RecipeManagerApplicationTest {

	@Autowired
	private RecipeController recipeController;
	@Test
	void contextLoads() {
		assertThat(recipeController).isNotNull();
	}

}
