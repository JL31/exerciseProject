package com.example.demo.usecases;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.tools.IDataModel;

import com.example.demo.usecases.createExercise.CreatedExercise;
import com.example.demo.usecases.getExercise.FetchedExercise;

import java.util.Optional;

public interface IExerciseServerSide {
    Optional<CreatedExercise> addExercise(IDataModel exerciseToAdd);
    Optional<FetchedExercise> getExercise(IDataModel exerciseToGet);
}
