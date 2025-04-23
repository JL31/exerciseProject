package com.jl31.exercise.usecases;

import com.jl31.exercise.tools.IDataModel;

import com.jl31.exercise.dataModels.Exercise;

import java.util.Optional;

public interface IExerciseServerSide {
    Optional<Exercise> addExercise(IDataModel exerciseToAdd);
    Optional<Exercise> getExercise(IDataModel exerciseToGet);
}
