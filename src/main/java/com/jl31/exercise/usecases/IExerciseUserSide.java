package com.jl31.exercise.usecases;

import com.jl31.exercise.tools.IDataModel;

import java.util.UUID;

public interface IExerciseUserSide {
    IUsecaseResponse createExercise(IDataModel providedExercise) throws Exception;
    IUsecaseResponse fetchExercise(UUID uuid) throws Exception;
}
