package org.example.domain;

import org.example.data.RecipeRepository;
import org.example.models.Ingredients;
import org.example.models.Measurements;
import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public List<Recipe> findAllRecipes() {
        return repository.findAllRecipes();
    }

    public Recipe findByRecipeId(int recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    public List<Ingredients> findAllIngredients() {
        return repository.findAllIngredients();
    }
    public List<Measurements> findAllMeasurements() {
        return repository.findAllMeasurements();
    }

    public List<RecipeIngredient> findIngredientsByRecipeId(int recipeId) { return repository.findIngredientsByRecipeId(recipeId); }

    public Result<Recipe> addRecipe(Recipe recipe) {
        Result<Recipe> result = validate(recipe);
        if (!result.isSuccess()) {
            return result;
        }

        if (recipe.getRecipeId() != 0) {
            result.addErrorMessage("recipeId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }


        recipe = repository.addRecipe(recipe);
        result.setPayload(recipe);
        return result;

    }

    public boolean deleteRecipe(int recipeId) {
        return repository.deleteRecipe(recipeId);

    }

    private Result validate(Recipe recipe) {
        Result result = new Result();

        if (recipe == null) {
            result.addErrorMessage("Recipe cannot be null.", ResultType.INVALID);
            return result;
        }

        if (recipe.getTitle() == null || recipe.getTitle().isBlank()) {
            result.addErrorMessage("Recipe title is required.", ResultType.INVALID);
        }

        if (recipe.getSteps() == null || recipe.getSteps().isBlank()) {
            result.addErrorMessage("Recipe steps are required.",
                    ResultType.INVALID);
        }

        if (recipe.getUserId() == 0) {
            result.addErrorMessage("User ID is required.", ResultType.INVALID);
        }

        if (recipe.getCookTimeMin() <= 0) {
            result.addErrorMessage("Cook time is required.", ResultType.INVALID);
        }

        if (recipe.getRecipeIngredientList().isEmpty() || recipe.getRecipeIngredientList() == null) {
            result.addErrorMessage("At least one ingredient is required.", ResultType.INVALID);
        }

        return result;

    }
}
