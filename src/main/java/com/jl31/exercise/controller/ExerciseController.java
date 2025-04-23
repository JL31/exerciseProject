package com.jl31.exercise.controller;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.tools.ResourceNotFoundException;

import com.jl31.exercise.usecases.IExerciseUserSide;
import com.jl31.exercise.usecases.IUsecaseResponse;

import com.jl31.exercise.usecases.createExercise.CreatedExercise;

import com.jl31.exercise.usecases.getExercise.FetchedExercise;

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
import java.util.UUID;

/**
 * Controller to handle exercises
 */
@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);
    private final IExerciseUserSide userSideAdapter;

    /**
     * Class constructor for exercises controller
     * @param userSideAdapter ...
     */
    public ExerciseController(IExerciseUserSide userSideAdapter) {
        this.userSideAdapter = userSideAdapter;
    }

    /**
     * Map the results of the API that creates an exercise to return data in a JSON-like format
     * @param createdExercise the result of the API that creates an exercise
     * @return the mapped results in a JSON-like format
     */
    private static Map<String, Object> createdExerciseMapping(CreatedExercise createdExercise) {
        Map<String, Object> mappingResult = new HashMap<>();

        mappingResult.put("uuid", createdExercise.exercise().uuid());

        return mappingResult;
    }

    /**
     * API to create an exercise from provided data
     * @param providedExercise exercise to be created details
     * @return an HTTP response containing the UUID of the created exercise (if everything went well) else an HTTP response detailing encountered error
     */
    @PostMapping("")
    public ResponseEntity<ApiResponseWithData> createExercise(@RequestBody Exercise providedExercise) {
        try {
            IUsecaseResponse createdExercise = this.userSideAdapter.createExercise(providedExercise);
            if (createdExercise instanceof CreatedExercise exercise) {
                return new ResponseEntity<>(
                    new ApiResponseWithData(
                        HttpStatus.CREATED.value(),
                        "Data successfully created",
                        createdExerciseMapping(exercise)
                    ),
                    HttpStatus.CREATED
                );
            } else {
                throw new Exception("Expected CreatedExercise but got something else");
            }
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

    /**
     * Map the results of the API that fetches an exercise to return data in a JSON-like format
     * @param fetchedExercise the result of the API that fetches an exercise
     * @return the mapped results in a JSON-like format
     */
    private static Map<String, Object> fetchedExerciseMapping(FetchedExercise fetchedExercise) {
        Map<String, Object> mappingResult = new HashMap<>();

        mappingResult.put("uuid", fetchedExercise.exercise().uuid());
        mappingResult.put("name", fetchedExercise.exercise().name());
        mappingResult.put("type", fetchedExercise.exercise().category());
        mappingResult.put("duration", fetchedExercise.exercise().duration());
        mappingResult.put("numberOfSeries", fetchedExercise.exercise().numberOfSeries());
        mappingResult.put("restDurationBetweenSeries", fetchedExercise.exercise().restDurationBetweenSeries());
        mappingResult.put("numberOfRepetitions", fetchedExercise.exercise().numberOfRepetitions());
        mappingResult.put("distance", fetchedExercise.exercise().distance());

        return mappingResult;
    }

    /**
     * API to get an existing exercise from provided UUID
     * @param uuid existing exercise UUID
     * @return an HTTP response containing the data of the fetched exercise (if everything went well) else an HTTP response detailing encountered error
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<ApiResponseWithData> getExercise(@PathVariable UUID uuid) {
        try {
            IUsecaseResponse fetchedExercise = this.userSideAdapter.fetchExercise(uuid);
            if (fetchedExercise instanceof FetchedExercise exercise) {
                return new ResponseEntity<>(
                    new ApiResponseWithData(
                        HttpStatus.OK.value(),
                        "Data successfully fetched",
                        fetchedExerciseMapping(exercise)
                    ),
                    HttpStatus.CREATED
                );
            } else {
                throw new Exception("Expected FetchedExercise but got something else");
            }
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
