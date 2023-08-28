package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.RecipeService;
import org.example.models.AppUser;
import org.example.models.Ingredients;
import org.example.models.Recipe;
import org.example.models.RecipeIngredient;
import org.example.security.JwtConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTest {

    @MockBean
    RecipeService service;

    @MockBean
    JwtConverter jwtConverter;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    void setUp() {
        AppUser mockAppUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", true, Collections.singletonList("USER"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockAppUser, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    @Test
    void findAllRecipesShouldReturn200() throws Exception {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(1, 1, 2, 10, 1);
        RecipeIngredient recipeIngredient1_2 = new RecipeIngredient(2, 1, 3, 100, 4);
        List<RecipeIngredient> ingredientList = List.of(recipeIngredient1, recipeIngredient1_2);

        RecipeIngredient recipeIngredient2 = new RecipeIngredient(1, 2, 2, 10, 1);
        RecipeIngredient recipeIngredient2_2 = new RecipeIngredient(2, 2, 3, 100, 4);
        List<RecipeIngredient> ingredientList2 = List.of(recipeIngredient2, recipeIngredient2_2);



        List<Recipe> recipes = List.of(
                new Recipe(1, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientList, "john@smith.com"),
                new Recipe(2, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientList2, "john@smith.com")
        );

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(recipes);

        when(service.findAllRecipes()).thenReturn(recipes);

        mvc.perform(get("/api/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void findAllIngredientsShouldReturn200() throws Exception {
        List<Ingredients> ingredients = List.of(
                new Ingredients(1, "Salt"),
                new Ingredients(2, "Pepper")
        );

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(ingredients);

        when(service.findAllIngredients()).thenReturn(ingredients);

        mvc.perform(get("/api/ingredients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));


    }

    @Test
    void findByRecipeIdShouldReturn200() throws Exception {
        RecipeIngredient recipeIngredient1 = new RecipeIngredient(1, 1, 2, 10, 1);
        RecipeIngredient recipeIngredient1_2 = new RecipeIngredient(2, 1, 3, 100, 4);
        List<RecipeIngredient> ingredientList = List.of(recipeIngredient1, recipeIngredient1_2);

        Recipe recipe = new Recipe(2, "Food Title", "Food steps", "placeholder for img", 20, 1, ingredientList, "john@smith.com");

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(recipe);

        when(service.findByRecipeId(2)).thenReturn(recipe);

        mvc.perform(get("/api/recipe/2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void findByRecipeIdShouldReturn400() throws Exception {
        int nonExistingRecipeId = 100;

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(nonExistingRecipeId);

        when(service.findByRecipeId(nonExistingRecipeId)).thenReturn(null);

        var request = get("/api/clearance/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    void addShouldReturn200() throws Exception {
    }

    @Test
    void addShouldReturn400() throws Exception {
    }

    @Test
    void deleteRecipeShouldReturn200() throws Exception {
    }

    @Test
    void deleteRecipeShouldReturn400() throws Exception {
    }
}