package com.jl31.exercise.serverSide;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.usecases.IExerciseServerSide;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jl31.exercise.tools.IDataModel;

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

/**
 * Defines the interface implementation that links the exercise usecases to the server side
 */
@Component
public class ServerSideAdapter implements IExerciseServerSide {

    private static final Logger logger = LoggerFactory.getLogger(ServerSideAdapter.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    /**
     *
     * @param dataSource defines the data source
     */
    public ServerSideAdapter (DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
    }

    /**
     * Creates an exercise into a database
     * @param exerciseToAdd the data to be inserted into a database
     * @return data for the successfully inserted exercise or nothing if something went wrong
     */
    @Override
    public Optional<Exercise> addExercise(IDataModel exerciseToAdd) {

        String additionQueryParameters = "INSERT INTO exercise (created_at, name, type, duration, number_of_series, rest_duration_between_series, number_of_repetitions, distance) ";
        String additionQueryValues = "VALUES (:createdAt, :name, :type, :duration, :numberOfSeries, :restDurationBetweenSeries, :numberOfRepetitions, :distance)";
        String additionQueryReturning = "RETURNING uuid";
        final String additionQuery = additionQueryParameters + additionQueryValues + additionQueryReturning;

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        OffsetDateTime insertionDatetime = zonedDateTime.toOffsetDateTime();

        if (!(exerciseToAdd instanceof Exercise(
            UUID uuid,
            String name,
            String category,
            Integer duration,
            Integer numberOfSeries,
            Integer restDurationBetweenSeries,
            Integer numberOfRepetitions,
            Integer distance
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
            return Optional.of(new Exercise(generatedUuid, null, null, null, null, null, null, null));
        } catch (Exception exception) {
            logger.error("Error while inserting exercise into database");
            logger.error(exception.getMessage());
            return Optional.empty();
        }

    }

    /**
     * Fetch an exercise from a database
     * @param exerciseToGet data as regards exercise to fetch from database (here contains an exercise UUID)
     * @return the data for the selected exercise
     */
    @Override
    public Optional<Exercise> getExercise(IDataModel exerciseToGet) {
        final String getQuery = "SELECT * FROM exercise WHERE uuid = :uuid";

        if (!(exerciseToGet instanceof Exercise(
            UUID uuid,
            String name,
            String category,
            Integer duration,
            Integer numberOfSeries,
            Integer restDurationBetweenSeries,
            Integer numberOfRepetitions,
            Integer distance
        ))) {
            throw new IllegalArgumentException("Incorrect usecase request type");
        }

        if (uuid == null) {
            throw new IllegalArgumentException("No provided uuid");
        }

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("uuid", uuid);

        try {
            Exercise exercise = jdbcTemplate.queryForObject(
                getQuery,
                parameterSource,
                new FetchedExerciseRowMapper()
            );
            return Optional.of(exercise);
        } catch (Exception exception) {
            logger.error("Error while fetching exercise from database with uuid : {}", uuid);
            logger.error(exception.getMessage());
            return Optional.empty();
        }

    }

}
