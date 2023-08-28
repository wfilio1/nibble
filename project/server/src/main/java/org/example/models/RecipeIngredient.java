package org.example.models;

import java.util.Objects;

public class RecipeIngredient {

    private int recipeIngredientId;
    private int recipeId;
    private int ingredientId;
    private int quantity;

    private int measurementId;

    public RecipeIngredient() {
    }

    public RecipeIngredient(int recipeIngredientId, int recipeId, int ingredientId, int quantity, int measurementId) {
        this.recipeIngredientId = recipeIngredientId;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.measurementId = measurementId;
    }

    public int getRecipeIngredientId() {
        return recipeIngredientId;
    }

    public void setRecipeIngredientId(int recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return recipeIngredientId == that.recipeIngredientId && recipeId == that.recipeId && ingredientId == that.ingredientId && quantity == that.quantity && measurementId == that.measurementId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeIngredientId, recipeId, ingredientId, quantity, measurementId);
    }
}
