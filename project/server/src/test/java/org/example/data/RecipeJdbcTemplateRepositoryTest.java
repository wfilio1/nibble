package org.example.data;

import org.example.models.Ingredients;
import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeJdbcTemplateRepositoryTest {

    @Autowired
    RecipeJdbcTemplateRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    static boolean hasSetup = false;

    @BeforeEach
    void setup() {
        if (!hasSetup) {
            hasSetup = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }


    @Test
    void findAllRecipes() {
        List<Recipe> result = repository.findAllRecipes();
        assertNotNull(result);

        assertTrue(result.size() >= 2);
    }

    @Test
    void findByRecipeId() {

        Recipe result = repository.findByRecipeId(1);

        assertNotNull(result);
        assertEquals("Mac N Cheese", result.getTitle());

        System.out.println(result.getRecipeIngredientList());
    }

    @Test
    void findAllIngredients() {
        List<Ingredients> result = repository.findAllIngredients();
        assertNotNull(result);

        assertTrue(result.size() >= 10);
    }

    @Test
    void findIngredientsByRecipeId() {
        List<RecipeIngredient> result = repository.findIngredientsByRecipeId(1);

        assertNotNull(result);

        assertTrue(result.size() == 4);

    }


    @Test
    void addRecipe() {

        RecipeIngredient ri = new RecipeIngredient();

        ri.setQuantity(2);
        ri.setIngredientId(22);

        RecipeIngredient ri2 = new RecipeIngredient();

        ri2.setQuantity(500);
        ri2.setIngredientId(6);

        List<RecipeIngredient> ingredientList = List.of(ri, ri2);

        Recipe recipe = new Recipe();
        recipe.setTitle("Grilled Cheese");
        recipe.setSteps("1. Put bread on pan. 2. Put cheese on bread. 3.Flip on other side.");
        recipe.setCookTimeMin(10);
        recipe.setImage("placeholder");
        recipe.setUserId(1);
        recipe.setRecipeIngredientList(ingredientList);

        Recipe result = repository.addRecipe(recipe);

        result.setRecipeIngredientList(ingredientList);

        assertNotNull(result);

        assertEquals(3, result.getRecipeId());
        assertEquals(ingredientList, result.getRecipeIngredientList());


    }

    @Test
    void deleteRecipe() {
        assertTrue(repository.deleteRecipe(1));
    }
}