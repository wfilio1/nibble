package org.example.data.mapper;

import org.example.models.Ingredients;
import org.example.models.LikedRecipes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikedRecipesMapper implements RowMapper<LikedRecipes> {

    @Override
    public LikedRecipes mapRow(ResultSet rs, int rowNum) throws SQLException {
        LikedRecipes likedRecipes = new LikedRecipes();

        likedRecipes.setLikedRecipesId(rs.getInt("liked_recipes_id"));
        likedRecipes.setRecipeId(rs.getInt("recipe_id"));
        likedRecipes.setUserId(rs.getInt("app_user_id"));

        return likedRecipes;
    }

}
