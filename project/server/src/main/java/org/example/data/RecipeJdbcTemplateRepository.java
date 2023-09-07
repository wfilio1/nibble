package org.example.data;

import org.example.data.mapper.IngredientsMapper;
import org.example.data.mapper.MeasurementsMapper;
import org.example.data.mapper.RecipeIngredientMapper;
import org.example.data.mapper.RecipeMapper;
import org.example.models.Ingredients;
import org.example.models.Measurements;
import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class RecipeJdbcTemplateRepository implements RecipeRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecipeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> findAllRecipes() {
//        final String sql = "select * from recipes order by recipe_id";

        final String sql = "select r.recipe_id, r.recipe_steps, r.recipe_title, r.recipe_image, r.recipe_cook_time, a.username, r.app_user_id from recipes r " +
                "join app_user a on r.app_user_id = a.app_user_id;";

        return jdbcTemplate.query(sql, new RecipeMapper());
    }

    @Override
    @Transactional
    public Recipe findByRecipeId(int recipeId) {

        final String sql = "select r.recipe_id, r.recipe_title, r.recipe_steps, r.recipe_cook_time, " +
                "r.recipe_image, a.username, r.app_user_id, ri.recipe_ingredients_id, i.ingredient_id, i.ingredient_name, ri.quantity, m.measurement_name from recipes r " +
                "join app_user a on r.app_user_id = a.app_user_id " +
                "join recipe_ingredients ri on ri.recipe_id = r.recipe_id " +
                "join measurements m on ri.measurement_id = m.measurement_id " +
                "join ingredients i on i.ingredient_id = ri.ingredient_id where r.recipe_id = ?;";

        Recipe result = jdbcTemplate.query(sql, new RecipeMapper(), recipeId).stream()
                .findAny().orElse(null);

        return result;
    }

//    private void findRecipeIngredients(Recipe recipe) {
//
//        final String sql = "select ri.recipe_ingredients_id, ri.recipe_id, ri.ingredient_id, i.ingredient_name, ri.quantity from recipe_ingredients ri " +
//                "join ingredients i on ri.ingredient_id = i.ingredient_id where ri.recipe_id = ?;";
//
//        var recipeIngredients = jdbcTemplate.query(sql, new RecipeIngredientMapper(), recipe.getRecipeId());
//
//        recipe.setRecipeIngredientList(recipeIngredients);
//    }

    @Override
    public List<Ingredients> findAllIngredients() {
        final String sql = "select * from ingredients order by ingredient_name";

        return jdbcTemplate.query(sql, new IngredientsMapper());
    }

    public List<Measurements> findAllMeasurements() {
        final String sql = "select * from measurements";

        return jdbcTemplate.query(sql, new MeasurementsMapper());
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {

        final String sql = "insert into recipes (recipe_title, recipe_steps, recipe_cook_time, recipe_image, app_user_id) " +
                "values(?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, recipe.getTitle());
            ps.setString(2, recipe.getSteps());
            ps.setInt(3, recipe.getCookTimeMin());
            ps.setString(4, recipe.getImage());
            ps.setInt(5, recipe.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        recipe.setRecipeId(keyHolder.getKey().intValue());


        for(int i = 0; i < recipe.getRecipeIngredientList().size(); i++ ) {
            addRecipeIngredients(recipe, recipe.getRecipeIngredientList().get(i));
        }


        return recipe;
    }

    @Override
    public RecipeIngredient addRecipeIngredients(Recipe recipe, RecipeIngredient recipeIngredient) {

        final String sql = "insert into recipe_ingredients (quantity, recipe_id, ingredient_id, measurement_id) values(?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, recipeIngredient.getQuantity());
            ps.setInt(2, recipe.getRecipeId());
            ps.setInt(3, recipeIngredient.getIngredientId());
            ps.setInt(4, recipeIngredient.getMeasurementId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        recipeIngredient.setRecipeIngredientId(keyHolder.getKey().intValue());

        return recipeIngredient;
    }

    @Override
    public List<RecipeIngredient> findIngredientsByRecipeId(int recipeId) {
        final String sql = "select * from recipe_ingredients where recipe_id = ?;";

        List<RecipeIngredient> result = jdbcTemplate.query(sql, new RecipeIngredientMapper(), recipeId);

        return result;

    }

    @Override
    @Transactional
    public boolean deleteRecipe(int recipeId) {
        jdbcTemplate.update("delete from recipe_ingredients where recipe_id = ?", recipeId);
        return jdbcTemplate.update("delete from recipes where recipe_id = ?;", recipeId) > 0;
    }



}
