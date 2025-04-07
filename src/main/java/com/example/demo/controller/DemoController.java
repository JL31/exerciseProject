package com.example.demo.controller;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.tools.ResourceNotFoundException;

import com.example.demo.usecases.IUsecaseResponse;
import com.example.demo.usecases.createExercise.CreateExerciseUsecase;

import com.example.demo.usecases.createExercise.CreatedExercise;
import com.example.demo.usecases.getExercise.FetchedExercise;
import com.example.demo.usecases.getExercise.GetExerciseUsecase;

import com.example.demo.dataModels.BaseExercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/exercise")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    private final CreateExerciseUsecase exerciseCreation;
    private final GetExerciseUsecase exerciseFetching;

    public DemoController(CreateExerciseUsecase exerciseCreation, GetExerciseUsecase exerciseFetching) {

        this.exerciseCreation = exerciseCreation;
        this.exerciseFetching = exerciseFetching;

    }

    private static Map<String, Object> createdExerciseMapping(CreatedExercise exercise) {
        Map<String, Object> mappingResult = new HashMap<>();

        mappingResult.put("uuid", exercise.exerciseUuid().uuid());

        return mappingResult;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponseWithData> createExercise(@RequestBody BaseExercise providedExercise) {
        try {
            Optional<IUsecaseResponse> createdExercise = this.exerciseCreation.execute(providedExercise);
            CreatedExercise exercise = createdExercise // TODO : bien comprendre la syntaxe
                .filter(CreatedExercise.class::isInstance)
                .map(CreatedExercise.class::cast)
                .orElseThrow(() -> new Exception("Issue during exercise insertion into database")
            );
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.CREATED.value(),
                    "Data successfully created",
                    createdExerciseMapping(exercise)
                ),
                HttpStatus.CREATED
            );
        } catch (IllegalArgumentException exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null
                ),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal server error",
                    null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private static Map<String, Object> fetchedExerciseMapping(FetchedExercise exercise) {
        Map<String, Object> mappingResult = new HashMap<>();

        mappingResult.put("uuid", exercise.exerciseUuid().uuid());
        mappingResult.put("name", exercise.baseExercise().name());
        mappingResult.put("type", exercise.baseExercise().category());
        mappingResult.put("duration", exercise.baseExercise().duration());
        mappingResult.put("numberOfSeries", exercise.baseExercise().numberOfSeries());
        mappingResult.put("restDurationBetweenSeries", exercise.baseExercise().restDurationBetweenSeries());
        mappingResult.put("numberOfRepetitions", exercise.baseExercise().numberOfRepetitions());
        mappingResult.put("distance", exercise.baseExercise().distance());

        return mappingResult;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponseWithData> getExercise(@PathVariable UUID uuid) {
        try {
            ExerciseUuid usecaseExercise = new ExerciseUuid(uuid);
            Optional<IUsecaseResponse> fetchedExercise = this.exerciseFetching.execute(usecaseExercise);
            FetchedExercise exercise = fetchedExercise
                .filter(FetchedExercise.class::isInstance)
                .map(FetchedExercise.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("Incorrect usecase request type")
            );
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.OK.value(),
                    "Data successfully fetched",
                    fetchedExerciseMapping(exercise)
                ),
                HttpStatus.OK
            );
        } catch (ResourceNotFoundException exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad request",
                    null
                ),
                HttpStatus.BAD_REQUEST
            );
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            return new ResponseEntity<>(
                new ApiResponseWithData(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal server error",
                    null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
