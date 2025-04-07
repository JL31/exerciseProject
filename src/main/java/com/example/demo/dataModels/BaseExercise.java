package com.example.demo.dataModels;

import com.example.demo.tools.IDataModel;

import java.util.HashMap;
import java.util.Map;

public record BaseExercise(
    String name,
    String category,
    int duration,
    int numberOfSeries,
    int restDurationBetweenSeries,
    int numberOfRepetitions,
    int distance
) implements IDataModel {
    public Map<String, Object> getData() {
        return new HashMap<String, Object>(Map.of(
            "name", name,
            "category", category,
            "duration", duration,
            "numberOfSeries", numberOfSeries,
            "restDurationBetweenSeries", restDurationBetweenSeries,
            "numberOfRepetitions", numberOfRepetitions,
            "distance", distance
        ));
    }
};
