package org.example.domain;

import org.example.data.CommentsRepository;
import org.example.models.Comments;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentsRepository commentsRepository;

    public CommentService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    public List<Comments> findByRecipeId(int recipeId) { return commentsRepository.findByRecipeId(recipeId); }

    public Result<Comments> addComment(Comments comment) {
        Result<Comments> result = validate(comment);
        if (!result.isSuccess()) {
            return result;
        }

        if (comment.getCommentId() != 0) {
            result.addErrorMessage("commentId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }


        comment = commentsRepository.addComment(comment);
        result.setPayload(comment);
        return result;

    }

    public boolean deleteComment(int commentId) {
        return commentsRepository.deleteComment(commentId);

    }

    private Result validate(Comments comment) {
        Result result = new Result();

        if (comment == null) {
            result.addErrorMessage("Comment cannot be null.", ResultType.INVALID);
            return result;
        }

        if (comment.getCommentInput() == null || comment.getCommentInput().isBlank()) {
            result.addErrorMessage("Comment input is required.", ResultType.INVALID);
        }

        if (comment.getAppUserId() == 0) {
            result.addErrorMessage("User ID is required.",
                    ResultType.INVALID);
        }

        if (comment.getRecipeId() == 0) {
            result.addErrorMessage("Recipe ID is required.", ResultType.INVALID);
        }

        return result;

    }
}
