package com.jl31.exercise.usecases;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.tools.ResourceNotFoundException;

import com.jl31.exercise.usecases.getExercise.FetchedExercise;
import com.jl31.exercise.usecases.getExercise.GetExerciseUsecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class GetExerciseUsecaseTest {

    @Mock
    private IExerciseServerSide serverSideAdapter;

    @InjectMocks
    private GetExerciseUsecase getExerciseUsecase;

    @Test
    public void testExecute_ExerciseFound() throws Exception {

        UUID uuid = UUID.randomUUID();
        Exercise testData = new Exercise(uuid, null, null, null, null, null, null, null);
        Exercise fetchedExercise = new Exercise(uuid, "name", "category", 30, 3, 60, 15, 100);

        when(serverSideAdapter.getExercise(testData)).thenReturn(Optional.of(fetchedExercise));

        IUsecaseResponse response = getExerciseUsecase.execute(testData);

        if (response instanceof FetchedExercise(Exercise exercise)) {
            assertEquals(fetchedExercise, exercise);
        }
    }

    @Test
    public void testExecute_ExerciseNotFound() {

        UUID uuid = UUID.randomUUID();
        Exercise testData = new Exercise(uuid, null, null, null, null, null, null, null);

        when(serverSideAdapter.getExercise(testData)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
            ResourceNotFoundException.class,
            () -> getExerciseUsecase.execute(testData)
        );

        String expectedMessage = "No exercise found with uuid : " + uuid +  " (may come from an unexpected error with database)";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testExecute_UnexpectedException() {

        UUID uuid = UUID.randomUUID();
        Exercise testData = new Exercise(uuid, null, null, null, null, null, null, null);

        when(serverSideAdapter.getExercise(testData)).thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(
            RuntimeException.class,
            () -> getExerciseUsecase.execute(testData)
        );

        String expectedMessage = "Unexpected error";
        assertEquals(expectedMessage, exception.getMessage());
    }

}
