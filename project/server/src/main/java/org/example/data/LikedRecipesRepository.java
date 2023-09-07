package org.example.data;

import org.example.models.LikedRecipes;

import java.util.List;

public interface LikedRecipesRepository {

    List<LikedRecipes> findByUserId(int userId);

    LikedRecipes addLikedRecipes(LikedRecipes likedRecipes);

    boolean deleteLikedRecipes(int likedRecipesId);

    LikedRecipes findByLikedRecipeId(int likedRecipeId);
}
