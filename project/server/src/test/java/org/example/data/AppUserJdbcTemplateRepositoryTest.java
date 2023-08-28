package org.example.data;

import org.example.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;


    static boolean hasSetup = false;
    @BeforeEach
    void setup() {
        if (!hasSetup) {
            hasSetup = true;
            jdbcTemplate.update("call set_known_good_state();");
        }
    }



    @Test
    void findAll() {
        List<AppUser> result = appUserRepository.findAll();
        assertNotNull(result);
    }

    @Test
    void findByUserId() {
        AppUser result = appUserRepository.findByUserId(1);
        assertNotNull(result);
        assertEquals("john@smith.com", result.getUsername());
    }

    @Test
    void findByUserName() {
        AppUser result = appUserRepository.findByUserName("john@smith.com");
        assertNotNull(result);
    }


    @Test
    void shouldCreate() {
        AppUser appUser = new AppUser();
        appUser.setUserName("Tony Bologni");
        appUser.setUserPassword("I <3 Bologni");
        appUser.setEnabled(true);

        AppUser result = appUserRepository.create(appUser);

        assertNotNull(result);
        assertEquals("Tony Bologni", result.getUsername());
    }

}