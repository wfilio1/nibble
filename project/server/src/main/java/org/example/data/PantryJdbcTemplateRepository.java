package org.example.data;


import org.example.models.Pantry;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PantryJdbcTemplateRepository implements PantryRepository{
    private final JdbcTemplate jdbcTemplate;
    public PantryJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<Pantry> pantryList = new ArrayList<>();

    private final RowMapper<Pantry> mapper = (resultSet, rowIndex) -> {
        Pantry pantry = new Pantry();

        pantry.setPantryId(resultSet.getInt("item_pantry_id"));
        pantry.setQuantity(resultSet.getInt("quantity"));
        pantry.setIngredientId(resultSet.getInt("ingredient_id"));
        pantry.setUserId(resultSet.getInt("app_user_id"));
        pantry.setMeasurementId(resultSet.getInt("measurement_id"));

        return pantry;
    };

    @Override
    public List<Pantry> findAll() throws DataAccessException {
        final String sql = "select item_pantry_id, quantity, ingredient_id, app_user_id, measurement_id " +
                "from item_pantry " +
                "order by item_pantry_id;";

        return jdbcTemplate.query(sql, mapper);
    }

//    @Override //original but it only pulls one pantry not all
//    public Pantry findByUserId(int app_user_id) throws DataAccessException {
//        final String sql = "select item_pantry_id, quantity, ingredient_id, app_user_id " +
//                "from item_pantry " +
//                "where app_user_id = ?";
//
////        return jdbcTemplate.query(sql,mapper, app_user_id).stream().findFirst().orElse(null);
//        List<Pantry> pantryList = jdbcTemplate.query(sql, mapper, app_user_id);
//
//        if (pantryList.isEmpty()) {
//            return null;
//        } else {
//            return pantryList.get(0);
//        }
//    }

    @Override
    public List<Pantry> findByUserId(int app_user_id) throws DataAccessException {
        final String sql = "select item_pantry_id, quantity, ingredient_id, app_user_id, measurement_id " +
                "from item_pantry " +
                "where app_user_id = ?";

        return jdbcTemplate.query(sql,mapper, app_user_id);

    }


    @Override //most likely going to return back an id instead of an ingredient name in frontend??
    public Pantry add(Pantry pantry) throws DataAccessException {
        final String sql = "insert into item_pantry (quantity, ingredient_id, app_user_id, measurement_id) " +
                "values (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, pantry.getQuantity());
            statement.setInt(2, pantry.getIngredientId());
            statement.setInt(3, pantry.getUserId());
            statement.setInt(4, pantry.getMeasurementId());
            return statement;
        }, keyHolder);

        if(rowsAffected == 0) {
            return null;
        }

        pantry.setPantryId((keyHolder.getKey().intValue()));
        pantryList.add(pantry);

        return pantry;

    }

    @Override
    public boolean delete(int item_pantry_id) throws DataAccessException {
        final String sql = "delete from item_pantry where item_pantry_id = ?";
        return jdbcTemplate.update(sql, item_pantry_id) > 0;
    }

}
