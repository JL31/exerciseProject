package com.jl31.exercise.usecases.getExercise;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.usecases.IUsecaseResponse;

/**
 * Structure to store data for a fetched exercise
 * @param exercise fetched exercise data
 */
public record FetchedExercise(Exercise exercise) implements IUsecaseResponse {}
