package org.example.data.mapper;

import org.example.models.Ingredients;
import org.example.models.RecipeIngredient;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientsMapper implements RowMapper<Ingredients> {


    @Override
    public Ingredients mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Ingredients ingredient = new Ingredients();

        ingredient.setIngredientId(resultSet.getInt("ingredient_id"));
        ingredient.setIngredientName(resultSet.getString("ingredient_name"));

        return ingredient;

    }
}
