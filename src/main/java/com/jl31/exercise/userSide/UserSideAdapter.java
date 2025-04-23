package com.jl31.exercise.userSide;

import com.jl31.exercise.dataModels.Exercise;
import com.jl31.exercise.tools.IDataModel;

import com.jl31.exercise.usecases.IExerciseUserSide;
import com.jl31.exercise.usecases.IUsecaseResponse;
import com.jl31.exercise.usecases.createExercise.CreateExerciseUsecase;
import com.jl31.exercise.usecases.getExercise.GetExerciseUsecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class UserSideAdapter implements IExerciseUserSide {
    private final CreateExerciseUsecase exerciseCreation;
    private final GetExerciseUsecase exerciseFetching;

    /**
     * Class constructor for user side adapter
     * @param exerciseCreation usecase to handle exercise creation
     */
    public UserSideAdapter(CreateExerciseUsecase exerciseCreation, GetExerciseUsecase exerciseFetching) {
        this.exerciseCreation = exerciseCreation;
        this.exerciseFetching = exerciseFetching;
    }

    private IDataModel mapDataToExerciseCreationUsecaseInput(IDataModel providedData) {
        return providedData;
    }

    public IUsecaseResponse createExercise(IDataModel providedExercise) throws Exception {
        IDataModel exerciseCreationUsecaseInput = mapDataToExerciseCreationUsecaseInput(providedExercise);
        return this.exerciseCreation.execute(exerciseCreationUsecaseInput);
    }

    private IDataModel mapDataToExerciseFetchingUsecaseInput(UUID uuid) {
        return new Exercise(uuid, null, null, null, null, null, null, null);
    }

    public IUsecaseResponse fetchExercise(UUID uuid) throws Exception {
        IDataModel exerciseFetchingUsecaseInput = mapDataToExerciseFetchingUsecaseInput(uuid);
        return this.exerciseFetching.execute(exerciseFetchingUsecaseInput);
    }

}
