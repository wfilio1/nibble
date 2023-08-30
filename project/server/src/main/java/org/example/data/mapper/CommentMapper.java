package org.example.data.mapper;

import org.example.models.Comments;
import org.example.models.Ingredients;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<Comments> {
    @Override
    public Comments mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Comments comment = new Comments();

        comment.setCommentId(resultSet.getInt("comment_id"));
        comment.setCommentInput(resultSet.getString("comment_input"));
        comment.setAppUserId(resultSet.getInt("app_user_id"));
        comment.setRecipeId(resultSet.getInt("recipe_id"));
        comment.setUsername(resultSet.getString("username"));

        return comment;
    }
}
