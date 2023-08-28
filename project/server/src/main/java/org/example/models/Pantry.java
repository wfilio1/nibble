package org.example.models;

import java.util.Objects;

public class Pantry {

    //need to change

    private int pantryId;
    private int quantity;

    int userId;
    int ingredientId;

    int measurementId;


    public Pantry() {
    }

    public Pantry(int pantryId, int quantity, int userId, int ingredientId, int measurementId) {
        this.pantryId = pantryId;
        this.quantity = quantity;
        this.userId = userId;
        this.ingredientId = ingredientId;
        this.measurementId = measurementId;
    }

    public int getPantryId() {
        return pantryId;
    }

    public void setPantryId(int pantryId) {
        this.pantryId = pantryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
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
        Pantry pantry = (Pantry) o;
        return pantryId == pantry.pantryId && quantity == pantry.quantity && userId == pantry.userId && ingredientId == pantry.ingredientId && measurementId == pantry.measurementId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pantryId, quantity, userId, ingredientId, measurementId);
    }
}
