package org.example.data;

import org.example.data.mapper.CommentMapper;
import org.example.models.Comments;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CommentsJdbcTemplateRepository implements CommentsRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentsJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comments> findByRecipeId(int recipeId) {
        final String sql = "select c.comment_id, c.comment_input, c.app_user_id, c.recipe_id, a.username " +
                "from comments c " +
                "join app_user a on a.app_user_id = c.app_user_id where c.recipe_id = ?;";

        return jdbcTemplate.query(sql, new CommentMapper(), recipeId);
    }

    @Override
    public Comments addComment(Comments comment) {
        final String sql = "insert into comments (comment_input, app_user_id, recipe_id) " +
                "values(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, comment.getCommentInput());
            ps.setInt(2, comment.getAppUserId());
            ps.setInt(3, comment.getRecipeId());

            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        comment.setCommentId(keyHolder.getKey().intValue());

        return comment;
    }

    @Override
    public boolean deleteComment(int commentId) {
        return jdbcTemplate.update("delete from comments where comment_id = ?", commentId) > 0;
    }
}
