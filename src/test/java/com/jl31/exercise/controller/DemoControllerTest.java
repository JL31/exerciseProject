package com.jl31.exercise.controller;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.tools.ResourceNotFoundException;
import com.jl31.exercise.usecases.createExercise.CreatedExercise;

import com.jl31.exercise.usecases.getExercise.FetchedExercise;

import com.jl31.exercise.usecases.IExerciseUserSide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class DemoControllerTest {

    @Mock
    private IExerciseUserSide userSideAdapter;

    private ExerciseController demoController;

    @BeforeEach
    void setUp() {
        demoController = new ExerciseController(userSideAdapter);
    }

    @Test
    void testCreateExercise_BadRequestBecauseOfInvalidProvidedRequest() throws Exception {

        Exercise mockInput = new Exercise(null, "12Pompes", "force", 30, 3, 60, 15, 0);
        String errorMessage = "Dummy invalid usecase request";
        when(userSideAdapter.createExercise(mockInput)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", Objects.requireNonNull(response.getBody()).getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testCreateExercise_BadRequestBecauseIssueDuringDatabaseInsertion() throws Exception {

        Exercise mockInput = new Exercise(null, "Pompes", "force", 30, 3, 60, 15, 0);
        String errorMessage = "Issue during exercise insertion into database";
        when(userSideAdapter.createExercise(mockInput)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", Objects.requireNonNull(response.getBody()).getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testCreateExercise_Returns201() throws Exception {

        Exercise mockInput = new Exercise(null, "abc", "def", 30, 3, 60, 15, 0);
        UUID uuid = UUID.randomUUID();
        CreatedExercise mockCreated = new CreatedExercise(
            new Exercise(
                uuid,
                mockInput.name(),
                mockInput.category(),
                mockInput.duration(),
                mockInput.numberOfSeries(),
                mockInput.restDurationBetweenSeries(),
                mockInput.numberOfRepetitions(),
                mockInput.distance()
            )
        );

        when(userSideAdapter.createExercise(mockInput)).thenReturn(mockCreated);

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Data successfully created", Objects.requireNonNull(response.getBody()).getMessage());
        Object returnedData = response.getBody().getData();
        if (returnedData instanceof Map<?, ?> mappedData) {
            @SuppressWarnings("unchecked")
            Map<String, Object> castedReturnedData = (Map<String, Object>) mappedData;
            assertEquals(uuid, castedReturnedData.get("uuid"));
        } else {
            throw new IllegalStateException("data field is not a Map => impossible to cast");
        }

    }

    @Test
    void testGetExercise_ResourceNotFound() throws Exception {

        UUID mockUuid = UUID.randomUUID();
        String errorMessage = "Fake error message raised with resource not found exception";
        when(userSideAdapter.fetchExercise(mockUuid)).thenThrow(new ResourceNotFoundException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.getExercise(mockUuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", Objects.requireNonNull(response.getBody()).getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testGetExercise_ValidCase() throws Exception {

        UUID mockUuid = UUID.randomUUID();
        Exercise mockExercise = new Exercise(mockUuid, "toto", "tutu", 1, 2, 3, 4, 5);
        FetchedExercise fetchedExercise = new FetchedExercise(mockExercise);
        when(userSideAdapter.fetchExercise(mockUuid)).thenReturn(fetchedExercise);

        ResponseEntity<ApiResponseWithData> response = demoController.getExercise(mockUuid);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Data successfully fetched", Objects.requireNonNull(response.getBody()).getMessage());
        Object returnedData = response.getBody().getData();
        if (returnedData instanceof Map<?, ?> mappedData) {
            @SuppressWarnings("unchecked")
            Map<String, Object> castedReturnedData = (Map<String, Object>) mappedData;
            assertEquals(mockUuid, castedReturnedData.get("uuid"));
            assertEquals("toto", castedReturnedData.get("name"));
            assertEquals("tutu", castedReturnedData.get("type"));
            assertEquals(1, castedReturnedData.get("duration"));
            assertEquals(2, castedReturnedData.get("numberOfSeries"));
            assertEquals(3, castedReturnedData.get("restDurationBetweenSeries"));
            assertEquals(4, castedReturnedData.get("numberOfRepetitions"));
            assertEquals(5, castedReturnedData.get("distance"));
        } else {
            throw new IllegalStateException("data field is not a Map => impossible to cast");
        }

    }

}
