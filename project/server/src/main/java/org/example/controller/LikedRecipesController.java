package org.example.controller;

import org.example.domain.AppUserService;
import org.example.domain.LikedRecipesService;
import org.example.domain.Result;
import org.example.models.AppUser;
import org.example.models.LikedRecipes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/liked")
@CrossOrigin
public class LikedRecipesController {

    private final LikedRecipesService likedRecipesService;

    private final AppUserService appUserService;

    public LikedRecipesController(LikedRecipesService likedRecipesService, AppUserService appUserService) {
        this.likedRecipesService = likedRecipesService;
        this.appUserService = appUserService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findByUserId(@PathVariable int userId) {
        List<LikedRecipes> likedRecipes = likedRecipesService.findByUserId(userId);
        if (likedRecipes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(likedRecipes);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody LikedRecipes likedRecipes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);
        likedRecipes.setUserId(appUser.getAppUserId());

        Result<LikedRecipes> result = likedRecipesService.addLikedRecipes(likedRecipes);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{likedRecipeId}")
    public ResponseEntity<Void> delete(@PathVariable int likedRecipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);

        LikedRecipes likedRecipe = likedRecipesService.findByLikedRecipeId(likedRecipeId);

        if(appUser.getAppUserId() != likedRecipe.getUserId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (likedRecipesService.deleteLikedRecipes(likedRecipeId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
