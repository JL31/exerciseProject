package com.example.demo.serverSide;

import com.example.demo.usecases.getExercise.FetchedExercise;
import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.dataModels.BaseExercise;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.UUID;

public class FetchedExerciseRowMapper implements RowMapper<FetchedExercise> {

    @Override
    public FetchedExercise mapRow(ResultSet queryResults, int rowNumber) throws SQLException {
        return new FetchedExercise(
            new ExerciseUuid(queryResults.getObject("uuid", UUID.class)),
            new BaseExercise(
                queryResults.getString("name"),
                queryResults.getString("type"),
                queryResults.getInt("duration"),
                queryResults.getInt("number_of_series"),
                queryResults.getInt("rest_duration_between_series"),
                queryResults.getInt("number_of_repetitions"),
                queryResults.getInt("distance")
            )
        );
    }
}
