package org.example.controller;


import org.apache.coyote.Response;
import org.example.domain.AppUserService;
import org.example.domain.PantryService;
import org.example.domain.Result;
import org.example.domain.ResultType;
import org.example.models.AppUser;
import org.example.models.Pantry;
import org.example.models.Recipe;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.List;

@RestController
@RequestMapping("/api/pantry")
@CrossOrigin
public class PantryController {

    private final PantryService pantryService;

    private final AppUserService appUserService;

    public PantryController(PantryService pantryService, AppUserService appUserService) {
        this.pantryService = pantryService;
        this.appUserService = appUserService;
    }

    @GetMapping //catch all
    public List<Pantry> findAll() throws DataAccessException {
        return pantryService.findAll();
    }

    @GetMapping("/personal")
    public List<Pantry> findPersonal() throws DataAccessException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);

        return pantryService.findByUserId(appUser.getAppUserId());
    }

    @PostMapping //add
    public ResponseEntity<?> addIngredient(@RequestBody Pantry pantry) throws DataAccessException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);
        pantry.setUserId(appUser.getAppUserId());

        Result result = pantryService.add(pantry);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED); // 201
        } else {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST); // 400
        }
    }

    @DeleteMapping("/delete/{pantryId}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable int pantryId) throws DataAccessException {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        AppUser appUser = (AppUser) appUserService.loadUserByUsername(username);

        List <Pantry> pantry = pantryService.findByUserId(appUser.getAppUserId());

        if(!pantry.isEmpty() || pantry != null) {
            if(appUser.getAppUserId() != pantry.get(0).getUserId()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(pantryService.delete(pantryId)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}



