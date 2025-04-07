package com.example.demo.usecases;

import com.example.demo.dataModels.BaseExercise;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.tools.IDataModel;
import com.example.demo.tools.RequestValidation;

import com.example.demo.usecases.createExercise.CreateExerciseUsecase;

import com.example.demo.usecases.createExercise.CreatedExercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateExerciseUsecaseTest {

    @Mock
    private IExerciseServerSide serverSideAdapter;

    @InjectMocks
    private CreateExerciseUsecase createExerciseUsecase;

    @Test
    public void testIsValidRequest_IncorrectUsecaseRequestType() {

        class TestData implements IDataModel {

            public Map<String, Object> getData() {
                return new HashMap<String, Object>(Map.of("testAttribute", "tutu"));
            }
        }
        TestData dataForTest = new TestData();

        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> { createExerciseUsecase.isValidRequest(dataForTest);}
        );

        assertEquals("Incorrect usecase request type", exception.getMessage());

    }

    @Test
    public void testIsValidRequest_InvalidName() {

        BaseExercise testData = new BaseExercise("name123", "category", 1, 2, 3, 4, 5);

        RequestValidation requestValidationResult = createExerciseUsecase.isValidRequest(testData);

        assertFalse(requestValidationResult.getRequestValidity());
        Map<String, String> expectedInvalidParameters = new HashMap<String, String>(Map.of("name", "name123"));
        assertEquals(expectedInvalidParameters, requestValidationResult.getInvalidParameters());

    }

    @Test
    public void testIsValidRequest_InvalidCategory() {

        BaseExercise testData = new BaseExercise("name", "category123", 1, 2, 3, 4, 5);

        RequestValidation requestValidationResult = createExerciseUsecase.isValidRequest(testData);

        assertFalse(requestValidationResult.getRequestValidity());
        Map<String, String> expectedInvalidParameters = new HashMap<String, String>(Map.of("category", "category123"));
        assertEquals(expectedInvalidParameters, requestValidationResult.getInvalidParameters());

    }

    @Test
    public void testIsValidRequest_ValidaCase() {

        BaseExercise testData = new BaseExercise("name", "category", 1, 2, 3, 4, 5);

        RequestValidation requestValidationResult = createExerciseUsecase.isValidRequest(testData);

        assertTrue(requestValidationResult.getRequestValidity());
        assertEquals(new HashMap<String, String>(), requestValidationResult.getInvalidParameters());

    }

    @Test
    public void testExecute_InvalidRequest() {

        BaseExercise testData = new BaseExercise("name123", "category", 1, 2, 3, 4, 5);

        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> { createExerciseUsecase.execute(testData);}
        );

        String expectedErrorMessage = "Invalid usecase request with following data : {\"name\":\"name123\"}";
        assertEquals(expectedErrorMessage, exception.getMessage());

    }

    @Test
    public void testExecute_ErrorInDatabaseProcessing() {

        BaseExercise testData = new BaseExercise("name", "category", 1, 2, 3, 4, 5);

        doThrow(new RuntimeException("Unexpected error in database processing")).when(serverSideAdapter).addExercise(testData);

        Exception exception = assertThrows(
            RuntimeException.class,
            () -> { createExerciseUsecase.execute(testData);}
        );

        assertEquals("Unexpected error in database processing", exception.getMessage());

    }

    @Test
    public void testExecute_NoReturnedUuid() throws Exception {

        BaseExercise testData = new BaseExercise("name", "category", 1, 2, 3, 4, 5);

        when(serverSideAdapter.addExercise(testData)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> { createExerciseUsecase.execute(testData);}
        );

        assertEquals("Issue during exercise insertion into database", exception.getMessage());

    }

    @Test
    public void testExecute_CorrectInsertion() throws Exception {

        BaseExercise testData = new BaseExercise("name", "category", 1, 2, 3, 4, 5);
        UUID createdUuid = UUID.randomUUID();
        CreatedExercise createdExercise = new CreatedExercise(new ExerciseUuid(createdUuid));

        when(serverSideAdapter.addExercise(testData)).thenReturn(Optional.of(createdExercise));

        Optional<IUsecaseResponse> response = createExerciseUsecase.execute(testData);

        assertTrue(response.isPresent());
        assertEquals(createdExercise, response.get());

    }

}
