package org.example.data;

import org.example.models.Comments;

import java.util.List;

public interface CommentsRepository {

    List<Comments> findByRecipeId(int recipeId);

    Comments addComment(Comments comment);

    boolean deleteComment(int commentId);
}
