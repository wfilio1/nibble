package org.example.data;

import org.example.models.Pantry;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface PantryRepository {

    List<Pantry> findAll() throws DataAccessException;
    List<Pantry> findByUserId(int app_user_id) throws DataAccessException;
    Pantry add(Pantry pantry) throws DataAccessException;
    boolean delete(int pantry_id) throws DataAccessException;





}
