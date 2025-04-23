package com.jl31.exercise.userSide;

import com.jl31.exercise.dataModels.Exercise;
import com.jl31.exercise.tools.IDataModel;

import com.jl31.exercise.usecases.IExerciseUserSide;
import com.jl31.exercise.usecases.IUsecaseResponse;
import com.jl31.exercise.usecases.createExercise.CreateExerciseUsecase;
import com.jl31.exercise.usecases.getExercise.GetExerciseUsecase;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * Defines the interface implementation that links the exercise usecases to the user side
 */
@Component
public class UserSideAdapter implements IExerciseUserSide {
    private final CreateExerciseUsecase exerciseCreation;
    private final GetExerciseUsecase exerciseFetching;

    /**
     * Class constructor for user side adapter
     * @param exerciseCreation usecase to handle exercise creation
     * @param exerciseFetching usecase to handle exercise fetching
     */
    public UserSideAdapter(CreateExerciseUsecase exerciseCreation, GetExerciseUsecase exerciseFetching) {
        this.exerciseCreation = exerciseCreation;
        this.exerciseFetching = exerciseFetching;
    }

    /**
     * Adapts provided data to the exercise creation usecase expected input format
     * @param providedData the provided data to be adapted (here some exercise data)
     * @return data that match the exercise creation usecase expected input format
     */
    private IDataModel mapDataToExerciseCreationUsecaseInput(IDataModel providedData) {
        return providedData;
    }

    /**
     * Creates an exercise from provided data
     * @param providedExercise the exercise data to be recorded
     * @return data resulting of exercise creation
     * @throws Exception generic exception to cover any exception that can occur during creation exercise process
     */
    public IUsecaseResponse createExercise(IDataModel providedExercise) throws Exception {
        IDataModel exerciseCreationUsecaseInput = mapDataToExerciseCreationUsecaseInput(providedExercise);
        return this.exerciseCreation.execute(exerciseCreationUsecaseInput);
    }

    /**
     * Adapts provided data to the exercise fetching usecase expected input format
     * @param uuid the provided data to be adapted (here an exercise UUID)
     * @return data that match the exercise fetching usecase expected input format
     */
    private IDataModel mapDataToExerciseFetchingUsecaseInput(UUID uuid) {
        return new Exercise(uuid, null, null, null, null, null, null, null);
    }

    /**
     * Fetches an exercise from its UUID
     * @param uuid an exercise UUID
     * @return data resulting of exercise fetching
     * @throws Exception generic exception to cover any exception that can occur during fetching exercise process
     */
    public IUsecaseResponse fetchExercise(UUID uuid) throws Exception {
        IDataModel exerciseFetchingUsecaseInput = mapDataToExerciseFetchingUsecaseInput(uuid);
        return this.exerciseFetching.execute(exerciseFetchingUsecaseInput);
    }

}
