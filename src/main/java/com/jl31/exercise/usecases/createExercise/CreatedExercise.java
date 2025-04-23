package com.jl31.exercise.usecases.createExercise;

import com.jl31.exercise.dataModels.Exercise;

import com.jl31.exercise.usecases.IUsecaseResponse;

public record CreatedExercise(Exercise exercise) implements IUsecaseResponse {}
