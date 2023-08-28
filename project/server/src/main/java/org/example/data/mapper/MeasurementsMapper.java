package org.example.data.mapper;

import org.example.models.Measurements;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class MeasurementsMapper implements RowMapper<Measurements> {


    @Override
    public Measurements mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Measurements measurements = new Measurements();

        measurements.setMeasurementId(resultSet.getInt("measurement_id"));
        measurements.setMeasurementName(resultSet.getString("measurement_name"));

        return measurements;

    }
}
