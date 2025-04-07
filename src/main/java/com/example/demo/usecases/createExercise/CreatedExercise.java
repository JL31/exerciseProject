package com.example.demo.usecases.createExercise;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.usecases.IUsecaseResponse;

public record CreatedExercise(ExerciseUuid exerciseUuid) implements IUsecaseResponse {}
