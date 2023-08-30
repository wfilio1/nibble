package org.example.controller;


import org.example.domain.AppUserService;
import org.example.domain.RecipeService;
import org.example.domain.Result;
import org.example.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RecipeController {

    private final RecipeService recipeService;

    private final AppUserService appUserService;

    public RecipeController(RecipeService recipeService, AppUserService appUserService) {
        this.recipeService = recipeService;
        this.appUserService = appUserService;
    }

    @GetMapping("/recipes")
    public List<Recipe> findAllRecipes() {
        return recipeService.findAllRecipes();
    }

    @GetMapping("/ingredients")
    public List<Ingredients> findAllIngredients() { return recipeService.findAllIngredients(); }

    @GetMapping("/measurements")
    public List<Measurements> findAllMeasurements() { return recipeService.findAllMeasurements(); }


    @GetMapping("/ingredients/{recipeId}")
    public ResponseEntity<Object> findIngredientsByRecipeId(@PathVariable int recipeId) {
        List<RecipeIngredient> recipeIngredient = recipeService.findIngredientsByRecipeId(recipeId);
        if (recipeIngredient == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(recipeIngredient);
    }


    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<Recipe> findByRecipeId(@PathVariable int recipeId) {
        Recipe recipe = recipeService.findByRecipeId(recipeId);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(recipe);
    }

    @PostMapping("/recipes")
    public ResponseEntity<Object> add(@RequestBody Recipe recipe) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);
        recipe.setUserId(appUser.getAppUserId());

        Result<Recipe> result = recipeService.addRecipe(recipe);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/recipes/{recipeId}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable int recipeId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);

        Recipe recipe = recipeService.findByRecipeId(recipeId);

        if(appUser.getAppUserId() != recipe.getUserId()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (recipeService.deleteRecipe(recipeId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
