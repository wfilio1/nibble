package org.example.domain;

import org.example.data.RecipeRepository;
import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    RecipeService service;

    @MockBean
    RecipeRepository repository;

    @Test
    void shouldFindByRecipeIdThatExists() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(2, 1, 3, 100, 4);

        List<RecipeIngredient> ingredientList = List.of(recipeIngredient, recipeIngredient2);
        Recipe expected = new Recipe(1, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientList, "john@smith.com");

        when(repository.findByRecipeId(1)).thenReturn(expected);

        Recipe actual = service.findByRecipeId(1);

        assertEquals(expected, actual);

        assertEquals("Food Title", expected.getTitle());
        assertEquals(1, expected.getRecipeId());
        assertEquals(ingredientList, expected.getRecipeIngredientList());

    }

    @Test
    void shouldNotFindRecipeIdThatDoesNotExist() {
        int nonExistingRecipeId = 1000;

        Recipe actual = service.findByRecipeId(nonExistingRecipeId);

        assertNull(actual);

    }

    @Test
    void findIngredientsByRecipeIdThatExists() {

        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(2, 1, 3, 100, 4);

        List<RecipeIngredient> ingredientList = List.of(recipeIngredient, recipeIngredient2);

        when(repository.findIngredientsByRecipeId(1)).thenReturn(ingredientList);

        List<RecipeIngredient> actual = service.findIngredientsByRecipeId(1);

        assertTrue(actual.contains(ingredientList.get(0)));
        assertTrue(actual.contains(ingredientList.get(1)));

    }

    @Test
    void shouldNotFindIngredientsByRecipeIdThatDoesNotExist(){
        int nonExistingRecipeId = 1000;

        List<RecipeIngredient> result = service.findIngredientsByRecipeId(1000);

        assertTrue(result.isEmpty());

    }



    @Test
    void shouldAddRecipe() {

        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        List<RecipeIngredient> ingredientListOut = List.of(recipeIngredient);

        Recipe recipeIn = new Recipe(0, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientListOut, "user");


        Recipe recipeOut = new Recipe(1, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientListOut, "user");

        when(repository.addRecipe(recipeIn)).thenReturn(recipeOut);

        Result<Recipe> result = service.addRecipe(recipeIn);

        assertTrue(result.isSuccess());

        assertEquals(recipeOut, result.getPayload());


    }

    @Test
    void shouldNotAddRecipeIfTitleIsEmpty() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        List<RecipeIngredient> ingredientListOut = List.of(recipeIngredient);

        Recipe recipeIn = new Recipe(0, "", "Food steps", "placeholder for img", 20, 1, ingredientListOut, "user");
        Result<Recipe> result = service.addRecipe(recipeIn);

        assertFalse(result.isSuccess());

        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("Recipe title is required."));

        assertNull(result.getPayload());


    }

    @Test
    void shouldNotAddRecipeIfStepsAreEmpty() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        List<RecipeIngredient> ingredientListOut = List.of(recipeIngredient);

        Recipe recipeIn = new Recipe(0, "Food Title", "", "placeholder for img", 20, 1, ingredientListOut, "user");
        Result<Recipe> result = service.addRecipe(recipeIn);

        assertFalse(result.isSuccess());

        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("Recipe steps are required."));

        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddRecipeIfCookTimeIsZero() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        List<RecipeIngredient> ingredientListOut = List.of(recipeIngredient);

        Recipe recipeIn = new Recipe(0, "Food Title", "Food steps", "placeholder for img", 0, 1, ingredientListOut, "user");
        Result<Recipe> result = service.addRecipe(recipeIn);

        assertFalse(result.isSuccess());

        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("Cook time is required."));

        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddRecipeIfUserIdIsZero() {
        RecipeIngredient recipeIngredient = new RecipeIngredient(1, 1, 2, 10, 1);

        List<RecipeIngredient> ingredientListOut = List.of(recipeIngredient);

        Recipe recipeIn = new Recipe(0, "Food Title", "Food steps", "placeholder for img", 20, 0, ingredientListOut, "user");
        Result<Recipe> result = service.addRecipe(recipeIn);

        assertFalse(result.isSuccess());

        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("User ID is required."));

        assertNull(result.getPayload());
    }

    @Test
    void shouldNotAddRecipeIfIngredientsListIsEmpty() {

        List<RecipeIngredient> ingredientListOut = List.of();

        Recipe recipeIn = new Recipe(0, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientListOut, "user");
        Result<Recipe> result = service.addRecipe(recipeIn);

        assertFalse(result.isSuccess());

        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("At least one ingredient is required."));

        assertNull(result.getPayload());
    }


    @Test
    void shouldDeleteRecipesThatExist() {
        when(repository.deleteRecipe(1)).thenReturn(true);

        boolean result = service.deleteRecipe(1);

        assertTrue(result);
    }

    @Test
    void shouldNotDeleteRecipesThatDoesNotExist() {
        when(repository.deleteRecipe(1)).thenReturn(false);

        boolean result = service.deleteRecipe(1);

        assertFalse(result);
    }
}