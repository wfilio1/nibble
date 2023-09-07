package org.example.data;

import org.example.data.mapper.LikedRecipesMapper;
import org.example.models.LikedRecipes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class LikedRecipesJdbcTemplateRepository implements LikedRecipesRepository {

    private final JdbcTemplate jdbcTemplate;

    public LikedRecipesJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<LikedRecipes> findByUserId(int userId) {
        final String sql = "select liked_recipes_id, recipe_id, app_user_id " +
                "from liked_recipes " +
                "where app_user_id = ?";

        return jdbcTemplate.query(sql, new LikedRecipesMapper(), userId);
    }

    @Override
    public LikedRecipes findByLikedRecipeId(int likedRecipeId) {
        final String sql = "select liked_recipes_id, recipe_id, app_user_id " +
                "from liked_recipes " +
                "where liked_recipes_id = ?";

        return jdbcTemplate.queryForObject(sql, new LikedRecipesMapper(), likedRecipeId);
    }

    @Override
    public LikedRecipes addLikedRecipes(LikedRecipes likedRecipes) {
        final String sql = "insert into liked_recipes (recipe_id, app_user_id) " +
                "values (?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, likedRecipes.getRecipeId());
            statement.setInt(2, likedRecipes.getUserId());
            return statement;
        }, keyHolder);

        if(rowsAffected == 0) {
            return null;
        }

        likedRecipes.setLikedRecipesId((keyHolder.getKey().intValue()));

        return likedRecipes;
    }

    @Override
    public boolean deleteLikedRecipes(int likedRecipesId) {
        return jdbcTemplate.update("delete from liked_recipes where liked_recipes_id = ?;", likedRecipesId) > 0;
    }

}
