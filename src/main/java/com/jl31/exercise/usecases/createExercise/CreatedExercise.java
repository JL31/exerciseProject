package com.jl31.exercise.usecases.createExercise;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.usecases.IUsecaseResponse;

/**
 * Structure to store exercise data about to be recorded
 * @param exercise the exercise data to be recorded
 */
public record CreatedExercise(Exercise exercise) implements IUsecaseResponse {}
