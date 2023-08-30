package org.example.models;

import java.util.Objects;

public class Comments {

    private int commentId;

    private String commentInput;

    private int appUserId;

    private String username;

    private int recipeId;


    public Comments() {
    }

    public Comments(int commentId, String commentInput, int appUserId, String username, int recipeId) {
        this.commentId = commentId;
        this.commentInput = commentInput;
        this.appUserId = appUserId;
        this.username = username;
        this.recipeId = recipeId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentInput() {
        return commentInput;
    }

    public void setCommentInput(String commentInput) {
        this.commentInput = commentInput;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comments comments)) return false;
        return commentId == comments.commentId && appUserId == comments.appUserId && recipeId == comments.recipeId && Objects.equals(commentInput, comments.commentInput) && Objects.equals(username, comments.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, commentInput, appUserId, username, recipeId);
    }
}
