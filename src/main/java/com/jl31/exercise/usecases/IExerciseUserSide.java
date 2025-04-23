package com.jl31.exercise.usecases;

import com.jl31.exercise.tools.IDataModel;

import java.util.UUID;

/**
 * Exercise user side interface
 */
public interface IExerciseUserSide {

    /**
     * Creates an exercise
     * @param providedExercise the exercise data to be recorded
     * @return an object that contains data for created exercise
     * @throws Exception generic exception to cover any possible exception that can occur during exercise creation
     */
    IUsecaseResponse createExercise(IDataModel providedExercise) throws Exception;

    /**
     * Returns a exercise data
     * @param uuid an exercise UUID
     * @return the exercise data associated to the provided exercise UUID
     * @throws Exception generic exception to cover any possible exception that can occur during exercise fetching
     */
    IUsecaseResponse fetchExercise(UUID uuid) throws Exception;
}
