package com.example.demo.usecases;

import com.example.demo.dataModels.BaseExercise;
import com.example.demo.dataModels.ExerciseUuid;

import com.example.demo.tools.ResourceNotFoundException;

import com.example.demo.usecases.getExercise.FetchedExercise;
import com.example.demo.usecases.getExercise.GetExerciseUsecase;

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
        ExerciseUuid exerciseUuid = new ExerciseUuid(uuid);
        FetchedExercise fetchedExercise = new FetchedExercise(
            exerciseUuid,
            new BaseExercise("name", "category", 30, 3, 60, 15, 100)
        );

        when(serverSideAdapter.getExercise(exerciseUuid)).thenReturn(Optional.of(fetchedExercise));

        Optional<IUsecaseResponse> response = getExerciseUsecase.execute(exerciseUuid);

        assertTrue(response.isPresent());
        assertEquals(fetchedExercise, response.get());
    }

    @Test
    public void testExecute_ExerciseNotFound() {
        UUID uuid = UUID.randomUUID();
        ExerciseUuid exerciseUuid = new ExerciseUuid(uuid);

        when(serverSideAdapter.getExercise(exerciseUuid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            getExerciseUsecase.execute(exerciseUuid);
        });

        String expectedMessage = "No exercise found with uuid : " + uuid;
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testExecute_UnexpectedException() {
        UUID uuid = UUID.randomUUID();
        ExerciseUuid exerciseUuid = new ExerciseUuid(uuid);

        when(serverSideAdapter.getExercise(exerciseUuid)).thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            getExerciseUsecase.execute(exerciseUuid);
        });

        String expectedMessage = "Unexpected error";
        assertEquals(expectedMessage, exception.getMessage());
    }

}
