package org.example.domain;

import org.example.data.PantryRepository;
import org.example.models.Pantry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PantryServiceTest {

    @Autowired
    PantryService pantryService;

    @MockBean
    PantryRepository pantryRepository;

    @Test
    void shouldFindValidUserOne() throws DataAccessException {
        List<Pantry> pantryList = new ArrayList<>();
        Pantry pantryItem = new Pantry(1, 10, 1, 1, 1);
        pantryList.add(pantryItem);

        when(pantryRepository.findByUserId(1)).thenReturn(pantryList);
        List<Pantry> pantry = pantryService.findByUserId(1);

        assertNotNull(pantry);
    }

    @Test
    void shouldNOTFindInvalidUser() throws DataAccessException {
        int fakeUserId = 999;
        when(pantryRepository.findByUserId(fakeUserId)).thenReturn(null);

        List<Pantry> result = pantryService.findByUserId(fakeUserId);

        assertNull(result);
    }

    @Test
    void shouldAddIngredientToValidPantry() throws DataAccessException {
        Pantry ingredientAdd = new Pantry(0, 10, 1, 1, 1);
        Pantry ingredientAfterAdd = new Pantry(1, 10, 1, 1, 1);
        when(pantryRepository.findByUserId(1)).thenReturn(null);
        when(pantryRepository.add(ingredientAdd)).thenReturn(ingredientAfterAdd);

        Result<Pantry> result = pantryService.add(ingredientAdd);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldNOTAddInvalidIngredientToPantry() throws DataAccessException {
        Pantry invalidIngredientAdd = new Pantry(0, 10, -1, 1, 1); // Invalid ingredient ID
        when(pantryRepository.findByUserId(1)).thenReturn(null);

        Result<Pantry> result = pantryService.add(invalidIngredientAdd);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
    }

    @Test
    void shouldDeleteValidIngredientInValidPantry() throws DataAccessException {
        List<Pantry> pantryListBeforeDelete = new ArrayList<>();
        pantryListBeforeDelete.add(new Pantry(1, 10, 1, 1, 1));

        when(pantryRepository.findByUserId(1)).thenReturn(pantryListBeforeDelete);
        when(pantryRepository.delete(1)).thenReturn(true);

        Result result = pantryService.delete(1);

        assertTrue(result.isSuccess());

    }

    @Test
    void shouldNOTDeleteInvalidIngredientInPantry() throws DataAccessException {

        when(pantryRepository.delete(-1)).thenReturn(false);

        Result result = pantryService.delete(-1); //invalid pantry id

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getResultType());
    }

    @Test
    void shouldAddIngredientIfQuantityValid() throws DataAccessException {
        Pantry ingredientToAdd = new Pantry(0, 5, 1, 1, 1); // Quantity is valid (greater than 0)
        Pantry ingredientAfterAdd = new Pantry(1, 5, 1, 1, 1);

        when(pantryRepository.add(ingredientToAdd)).thenReturn(ingredientAfterAdd);

        Result<Pantry> result = pantryService.add(ingredientToAdd);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(ingredientAfterAdd, result.getPayload());
    }

    @Test
    void shouldNOTAddIngredientIfQuantityZero() throws DataAccessException {
        Pantry ingredientToAdd = new Pantry(0, 0, 1, 1,1); // Quantity is zero
        Result<Pantry> result = pantryService.add(ingredientToAdd);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        assertTrue(result.getErrorMessages().contains("Quantity cannot be zero or negative"));
    }

}


