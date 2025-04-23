package com.jl31.exercise.serverSide;

import com.jl31.exercise.dataModels.Exercise;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;

/**
 * Maps results for a fetched exercise into some data models
 */
public class FetchedExerciseRowMapper implements RowMapper<Exercise> {

    @Override
    public Exercise mapRow(ResultSet queryResults, int rowNumber) throws SQLException {
        return new Exercise(
            queryResults.getObject("uuid", UUID.class),
            queryResults.getString("name"),
            queryResults.getString("type"),
            queryResults.getInt("duration"),
            queryResults.getInt("number_of_series"),
            queryResults.getInt("rest_duration_between_series"),
            queryResults.getInt("number_of_repetitions"),
            queryResults.getInt("distance")
        );
    }
}
