package org.example.domain;

import org.example.data.LikedRecipesRepository;
import org.example.models.LikedRecipes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikedRecipesService {

    private final LikedRecipesRepository likedRecipesRepository;

    public LikedRecipesService(LikedRecipesRepository likedRecipesRepository) {
        this.likedRecipesRepository = likedRecipesRepository;
    }

    public List<LikedRecipes> findByUserId(int userId) {
        return likedRecipesRepository.findByUserId(userId);
    }

    public LikedRecipes findByLikedRecipeId(int likedRecipeId) {
        return likedRecipesRepository.findByLikedRecipeId(likedRecipeId);
    }

    public Result<LikedRecipes> addLikedRecipes(LikedRecipes likedRecipes) {
        Result<LikedRecipes> result = validate(likedRecipes);

        if (!result.isSuccess()) {
            return result;
        }

        if (likedRecipes.getLikedRecipesId() != 0) {
            result.addErrorMessage("ID cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }


        likedRecipes = likedRecipesRepository.addLikedRecipes(likedRecipes);
        result.setPayload(likedRecipes);
        return result;
    }

    public boolean deleteLikedRecipes(int likedRecipesId) {
        return likedRecipesRepository.deleteLikedRecipes((likedRecipesId));
    }

    private Result validate(LikedRecipes likedRecipes) {
        Result result = new Result();

        if (likedRecipes == null) {
            result.addErrorMessage("Liked recipe cannot be null.", ResultType.INVALID);
            return result;
        }

        if (likedRecipes.getRecipeId() == 0) {
            result.addErrorMessage("This recipe does not exist.", ResultType.INVALID);
        }

        if (likedRecipes.getUserId() == 0) {
            result.addErrorMessage("User ID is required.", ResultType.INVALID);
        }

        return result;

    }
}
