package org.example.data;

import org.example.models.Pantry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PantryJdbcTemplateRepositoryTest {

    @Autowired
    private PantryRepository pantryRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    static boolean hasSetup = false;

    @BeforeEach
    void setup() {
        if (!hasSetup) {
            hasSetup = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }

    @Test //cant test until database is fixed//this is my checker for database connection
    void shouldFindAll() throws DataAccessException {
        List<Pantry> result = pantryRepository.findAll();
        assertNotNull(result);
        assertTrue(result.size() >= 1);
    }

    @Test
    void shouldFindPantryByExistingUserId() throws DataAccessException {
        List<Pantry> result = pantryRepository.findByUserId(1);//existing id in database
        assertNotNull(result);
        Pantry firstPantryItem = result.get(0); //only want at least 1 finding
        assertEquals(1, firstPantryItem.getPantryId());
        assertEquals(50, firstPantryItem.getQuantity());
        assertEquals(1, firstPantryItem.getIngredientId());
        assertEquals(1, firstPantryItem.getUserId());
        assertEquals(1, firstPantryItem.getMeasurementId());
    }

    @Test
    void shouldNOTFindPantryWithFakeId() throws DataAccessException {
        int fakeUserId = 999;
        List<Pantry> result = pantryRepository.findByUserId(fakeUserId);

        assertTrue(result.isEmpty()); //should be empty for non existing users
    }

}
