package org.example.models;

import java.util.Objects;

public class LikedRecipes {

    private int likedRecipesId;

    private int recipeId;

    private int userId;

    public LikedRecipes() {}

    public LikedRecipes(int likedRecipesId, int recipeId, int userId) {
        this.likedRecipesId = likedRecipesId;
        this.recipeId = recipeId;
        this.userId = userId;
    }

    public int getLikedRecipesId() {
        return likedRecipesId;
    }

    public void setLikedRecipesId(int likedRecipesId) {
        this.likedRecipesId = likedRecipesId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikedRecipes that)) return false;
        return likedRecipesId == that.likedRecipesId && recipeId == that.recipeId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(likedRecipesId, recipeId, userId);
    }
}
