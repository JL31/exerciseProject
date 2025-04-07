package com.example.demo.usecases.getExercise;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.dataModels.BaseExercise;
import com.example.demo.usecases.IUsecaseResponse;

public record FetchedExercise(ExerciseUuid exerciseUuid, BaseExercise baseExercise) implements IUsecaseResponse {}
