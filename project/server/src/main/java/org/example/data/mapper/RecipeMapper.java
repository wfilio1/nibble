package org.example.data.mapper;

import org.example.models.Recipe;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeMapper implements RowMapper<Recipe> {

    @Override
    public Recipe mapRow(ResultSet resultSet, int i) throws SQLException {
        Recipe recipe = new Recipe();

        recipe.setRecipeId(resultSet.getInt("recipe_id"));
        recipe.setTitle(resultSet.getString("recipe_title"));
        recipe.setSteps(resultSet.getString("recipe_steps"));
        recipe.setCookTimeMin(resultSet.getInt("recipe_cook_time"));
        recipe.setImage(resultSet.getString("recipe_image"));
        recipe.setUserId(resultSet.getInt("app_user_id"));
        recipe.setUsername(resultSet.getString("username"));

        return recipe;
    }

}
