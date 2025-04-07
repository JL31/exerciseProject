package com.example.demo.controller;

import com.example.demo.dataModels.BaseExercise;
import com.example.demo.dataModels.ExerciseUuid;

import com.example.demo.tools.ResourceNotFoundException;
import com.example.demo.usecases.createExercise.CreateExerciseUsecase;
import com.example.demo.usecases.createExercise.CreatedExercise;

import com.example.demo.usecases.getExercise.FetchedExercise;
import com.example.demo.usecases.getExercise.GetExerciseUsecase;

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
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class DemoControllerTest {

    @Mock
    private CreateExerciseUsecase createExerciseUsecase;

    @Mock
    private GetExerciseUsecase getExerciseUsecase;

    private DemoController demoController;

    @BeforeEach
    void setUp() {
        demoController = new DemoController(createExerciseUsecase, getExerciseUsecase);
    }

    @Test
    void testCreateExercise_BadRequestBecauseOfInvalidProvidedRequest() throws Exception {

        BaseExercise mockInput = new BaseExercise("12Pompes", "force", 30, 3, 60, 15, 0);
        String errorMessage = "Dummy invalid usecase request";
        when(createExerciseUsecase.execute(mockInput)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody().getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testCreateExercise_BadRequestBecauseIssueDuringDatabaseInsertion() throws Exception {

        BaseExercise mockInput = new BaseExercise("Pompes", "force", 30, 3, 60, 15, 0);
        String errorMessage = "Issue during exercise insertion into database";
        when(createExerciseUsecase.execute(mockInput)).thenThrow(new IllegalArgumentException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody().getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testCreateExercise_Returns201() throws Exception {

        BaseExercise mockInput = new BaseExercise("abc", "def", 30, 3, 60, 15, 0);
        UUID uuid = UUID.randomUUID();
        CreatedExercise mockCreated = new CreatedExercise(new ExerciseUuid(uuid));

        when(createExerciseUsecase.execute(mockInput)).thenReturn(Optional.of(mockCreated));

        ResponseEntity<ApiResponseWithData> response = demoController.createExercise(mockInput);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Data successfully created", response.getBody().getMessage());
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
        ExerciseUuid mockInput = new ExerciseUuid(mockUuid);
        String errorMessage = "Fake error message raised with resource not found exception";
        when(getExerciseUsecase.execute(mockInput)).thenThrow(new ResourceNotFoundException(errorMessage));

        ResponseEntity<ApiResponseWithData> response = demoController.getExercise(mockUuid);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody().getMessage());
        Object returnedData = response.getBody().getData();
        assertNull(returnedData);

    }

    @Test
    void testGetExercise_ValidCase() throws Exception {

        UUID mockUuid = UUID.randomUUID();
        ExerciseUuid mockInput = new ExerciseUuid(mockUuid);
        FetchedExercise fetchedExercise = new FetchedExercise(
            new ExerciseUuid(mockUuid),
            new BaseExercise("toto", "tutu", 1, 2, 3, 4, 5)
        );
        when(getExerciseUsecase.execute(mockInput)).thenReturn(Optional.of(fetchedExercise));

        ResponseEntity<ApiResponseWithData> response = demoController.getExercise(mockUuid);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data successfully fetched", response.getBody().getMessage());
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
