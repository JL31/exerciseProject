package com.jl31.exercise.usecases;

import com.jl31.exercise.tools.IDataModel;

import com.jl31.exercise.dataModels.Exercise;

import java.util.Optional;

/**
 * Exercise server side interface
 */
public interface IExerciseServerSide {

    /**
     * Adds an exercise into a data structure
     * @param exerciseToAdd the data to be added
     * @return The added exercise (with its UUID) or nothing if something went wrong during recording process
     */
    Optional<Exercise> addExercise(IDataModel exerciseToAdd);

    /**
     * Fetch exercise data from a data structure
     * @param exerciseToGet the exercise relevant data to fetch an exercise
     * @return The fetched exercise data or nothing is something sent wrong during fetch process
     */
    Optional<Exercise> getExercise(IDataModel exerciseToGet);
}
