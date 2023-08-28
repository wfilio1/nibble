package org.example.data;

import org.example.models.*;

import java.util.List;

public interface RecipeRepository {

    List<Recipe> findAllRecipes();

    Recipe findByRecipeId(int recipeId);

    List<Ingredients> findAllIngredients();

    Recipe addRecipe(Recipe recipe);

    boolean deleteRecipe(int recipeId);

    RecipeIngredient addRecipeIngredients(Recipe recipe, RecipeIngredient recipeIngredient);
    List<RecipeIngredient> findIngredientsByRecipeId(int recipeId);

    List<Measurements> findAllMeasurements();





}
