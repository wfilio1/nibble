package org.example.controller;


import org.apache.coyote.Response;
import org.example.domain.AppUserService;
import org.example.domain.PantryService;
import org.example.domain.Result;
import org.example.domain.ResultType;
import org.example.models.AppUser;
import org.example.models.Pantry;
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

//    @GetMapping("/{userId}")
//    public ResponseEntity<Pantry> findPantryByUserId(@PathVariable int userId) throws DataAccessException {
//        Pantry pantry = pantryService.findbyUserId(userId);
//        if (pantry == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(pantry, HttpStatus.OK);
//    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<Pantry>> findPantryByUserId(@PathVariable int userId) throws DataAccessException {
//        List<Pantry> pantryList = pantryService.findbyUserId(userId);
//
//        if (pantryList == null || pantryList.isEmpty()) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//
//        return new ResponseEntity<>(pantryList, HttpStatus.OK);
//    }

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
        Result result = pantryService.delete(pantryId);
        if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
    }
}



