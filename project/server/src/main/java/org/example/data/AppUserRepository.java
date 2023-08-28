package org.example.data;

import org.example.models.AppUser;

import java.util.List;

public interface AppUserRepository {

    List<AppUser> findAll();

    AppUser findByUserId(int userId);

    AppUser findByUserName(String userName);

    AppUser create(AppUser appUser);

}
