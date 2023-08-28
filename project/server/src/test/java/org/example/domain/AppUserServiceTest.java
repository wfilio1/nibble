package org.example.domain;

import org.example.data.AppUserRepository;
import org.example.models.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AppUserServiceTest {

    @Autowired
    AppUserService service;
    @MockBean
    AppUserRepository repository;

    @Test
    void findByUserName() {
        AppUser expectedUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", true, List.of("ADMIN"));

        when(repository.findByUserName("john@smith.com")).thenReturn(expectedUser);

        AppUser actual = (AppUser) service.loadUserByUsername("john@smith.com");


        assertEquals(expectedUser, actual);
        assertEquals("P@ssw0rd!", expectedUser.getPassword());
        assertEquals(1, expectedUser.getAppUserId());

    }

    @Test
    void create() {

        AppUser expectedUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", true, List.of("ADMIN"));

        when(repository.create(any())).thenReturn(expectedUser);

        AppUser actual = service.create("john@smith.com", "P@ssw0rd!").getPayload();
        Result<AppUser> result = service.create("john@smith.com", "P@ssw0rd!");

        assertEquals(expectedUser, actual);
        assertEquals(expectedUser, result.getPayload());
    }

    @Test
    void shouldNotCreate() {

        AppUser expectedUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", true, List.of("ADMIN"));

        when(repository.create(any())).thenReturn(null);

        AppUser actual = service.create("john@smith.com", "P@ssw0rd!").getPayload();
        Result<AppUser> result = service.create("john@smith.com", "P@ssw0rd!");

        assertNotEquals(expectedUser, actual);
        assertNotEquals(expectedUser, result.getPayload());

    }

    @Test
    void shouldNotCreateNullPassword() {

        String userName = "John.com";
        String password = null;

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateNullUsername() {

        String userName = null;
        String password = "Password";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateDuplicate() {
        AppUser expectedUser = new AppUser(1, "john@smith.com", "P@ssw0rd!", true, List.of("ADMIN"));

        when(repository.create(any())).thenReturn(expectedUser);
        when(repository.create(any())).thenThrow(DuplicateKeyException.class);

        Result<AppUser> result = service.create("john@smith.com", "P@ssw0rd!");

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("The provided username already exists"));
    }

    @Test
    void shouldNotCreateWithUserNameLongerThan50Chars() {

        String userName = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        String password = "Password";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotCreateIfUserNameDoesNotContainAtChar() {
        String userName = "NotRealEmailUser";
        String password = "Password";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Username must contain an @ symbol."));
    }

    @Test
    void shouldNotCreateIfPasswordLessThan8Chars() {
        String userName = "JohnWayne@Gmail.com";
        String password = "NoU!?";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
        assertFalse(password.length() > 8);

    }

    @Test
    void shouldNotCreateIfPasswordContainsNoDigits() {
        String userName = "JohnWayne@Gmail.com";
        String password = "abcdefghijklmnopqrstuvwxyz!?";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
        assertTrue(password.length() > 8);
    }

    @Test
    void shouldNotCreateIfPasswordContainsNoAlphaChars() {
        String userName = "JohnWayne@Gmail.com";
        String password = "0123456789!?";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
        assertTrue(password.length() > 8);
    }

    @Test
    void shouldNotCreateIfPasswordContainsNoNonDigitOrAlphaChars() {
        String userName = "JohnWayne@Gmail.com";
        String password = "0123456789abcdefgh";

        Result<AppUser> result = service.create(userName, password);

        assertFalse(result.isSuccess());
        assertTrue(password.length() > 8);
    }
}