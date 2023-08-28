package org.example.data.mapper;

import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeIngredientMapper implements RowMapper<RecipeIngredient> {

    @Override
    public RecipeIngredient mapRow(ResultSet resultSet, int i) throws SQLException {
        RecipeIngredient recipeIngredient = new RecipeIngredient();

        recipeIngredient.setRecipeIngredientId(resultSet.getInt("recipe_ingredients_id"));
        recipeIngredient.setQuantity(resultSet.getInt("quantity"));
        recipeIngredient.setRecipeId(resultSet.getInt("recipe_id"));
        recipeIngredient.setIngredientId(resultSet.getInt("ingredient_id"));
        recipeIngredient.setMeasurementId(resultSet.getInt("measurement_id"));

        return recipeIngredient;

    }

}
