package org.example.models;

import java.util.List;
import java.util.Objects;

public class Recipe {

    private int recipeId;
    private String title;
    private String steps;
    private String image;
    private int cookTimeMin;

    private int userId;
    private String username;
    private List<RecipeIngredient> recipeIngredientList;

    public Recipe() {}

    public Recipe(int recipeId, String title, String steps, String image,
                  int cookTimeMin, int userId, List<RecipeIngredient> recipeIngredientList, String username) {
        this.recipeId = recipeId;
        this.title = title;
        this.steps = steps;
        this.image = image;
        this.cookTimeMin = cookTimeMin;
        this.userId = userId;
        this.recipeIngredientList = recipeIngredientList;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCookTimeMin() {
        return cookTimeMin;
    }

    public void setCookTimeMin(int cookTimeMin) {
        this.cookTimeMin = cookTimeMin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<RecipeIngredient> getRecipeIngredientList() {
        return recipeIngredientList;
    }

    public void setRecipeIngredientList(List<RecipeIngredient> recipeIngredientList) {
        this.recipeIngredientList = recipeIngredientList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return recipeId == recipe.recipeId && cookTimeMin == recipe.cookTimeMin && userId == recipe.userId && Objects.equals(title, recipe.title) && Objects.equals(steps, recipe.steps) && Objects.equals(image, recipe.image) && Objects.equals(username, recipe.username) && Objects.equals(recipeIngredientList, recipe.recipeIngredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, title, steps, image, cookTimeMin, userId, username, recipeIngredientList);
    }
}
