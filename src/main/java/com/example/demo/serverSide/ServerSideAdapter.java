package com.example.demo.serverSide;

import com.example.demo.dataModels.BaseExercise;
import com.example.demo.dataModels.ExerciseUuid;

import com.example.demo.usecases.IExerciseServerSide;
import com.example.demo.usecases.createExercise.CreatedExercise;
import com.example.demo.usecases.getExercise.FetchedExercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.tools.IDataModel;

import javax.sql.DataSource;

import java.time.ZonedDateTime;
import java.time.OffsetDateTime;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import org.springframework.stereotype.Component;


@Component
public class ServerSideAdapter implements IExerciseServerSide {

    private static final Logger logger = LoggerFactory.getLogger(ServerSideAdapter.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public ServerSideAdapter (DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    @Override
    public Optional<CreatedExercise> addExercise(IDataModel exerciseToAdd) {

        String additionQueryParameters = "INSERT INTO exercise (created_at, name, type, duration, number_of_series, rest_duration_between_series, number_of_repetitions, distance) ";
        String additionQueryValues = "VALUES (:createdAt, :name, :type, :duration, :numberOfSeries, :restDurationBetweenSeries, :numberOfRepetitions, :distance)";
        String additionQueryReturning = "RETURNING uuid";
        final String additionQuery = additionQueryParameters + additionQueryValues + additionQueryReturning;

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        OffsetDateTime insertionDatetime = zonedDateTime.toOffsetDateTime();

        if (!(exerciseToAdd instanceof BaseExercise(
            String name,
            String category,
            int duration,
            int numberOfSeries,
            int restDurationBetweenSeries,
            int numberOfRepetitions,
            int distance
        ))) {
            throw new IllegalArgumentException("Incorrect usecase request type");
        }

        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("createdAt", insertionDatetime);
        queryParameters.put("name", name);
        queryParameters.put("type", category);
        queryParameters.put("duration", duration);
        queryParameters.put("numberOfSeries", numberOfSeries);
        queryParameters.put("restDurationBetweenSeries", restDurationBetweenSeries);
        queryParameters.put("numberOfRepetitions", numberOfRepetitions);
        queryParameters.put("distance", distance);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource(queryParameters);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(additionQuery, parameterSource, keyHolder);
            UUID generatedUuid = (UUID) keyHolder.getKeys().get("uuid");
            return Optional.of(new CreatedExercise(new ExerciseUuid(generatedUuid)));
        } catch (Exception exception) {
            logger.error("Error while inserting exercise into database");
            logger.error(exception.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public Optional<FetchedExercise> getExercise(IDataModel exerciseToGet) {
        final String getQuery = "SELECT * FROM exercise WHERE uuid = :uuid";

        if (!(exerciseToGet instanceof ExerciseUuid(UUID uuid))) {
            throw new IllegalArgumentException("Incorrect usecase request type");
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("uuid", uuid);

        try {
            FetchedExercise exercise = jdbcTemplate.queryForObject(
                getQuery,
                parameterSource,
                new FetchedExerciseRowMapper()
            );
            return Optional.of(exercise);
        } catch (Exception exception) {
            logger.error("Error while fetching exercise from database with uuid : " + uuid);
            logger.error(exception.getMessage());
            return Optional.empty();
        }

    }

}
