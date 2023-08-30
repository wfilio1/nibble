package org.example.controller;

import org.example.domain.AppUserService;
import org.example.domain.CommentService;
import org.example.domain.Result;
import org.example.models.AppUser;
import org.example.models.Comments;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    private final AppUserService appUserService;

    public CommentController(CommentService commentService, AppUserService appUserService) {
        this.commentService = commentService;
        this.appUserService = appUserService;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Object> findByRecipeId(@PathVariable int recipeId) {
        List<Comments> comments = commentService.findByRecipeId(recipeId);
        if (comments == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Comments comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);
        comment.setAppUserId(appUser.getAppUserId());

        Result<Comments> result = commentService.addComment(comment);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId) throws DataAccessException {
        if (commentService.deleteComment(commentId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
