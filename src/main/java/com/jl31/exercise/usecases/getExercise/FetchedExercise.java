package com.jl31.exercise.usecases.getExercise;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.usecases.IUsecaseResponse;

public record FetchedExercise(Exercise exercise) implements IUsecaseResponse {}
