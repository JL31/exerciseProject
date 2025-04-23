package com.jl31.exercise.dataModels;

import com.jl31.exercise.tools.IDataModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Contains data for an exercise
 * @param uuid exercise UUID
 * @param name exercise name
 * @param category exercise category
 * @param duration duration expressed in seconds
 * @param numberOfSeries number of series (for example if Tabata method used)
 * @param restDurationBetweenSeries rest time expressed in seconds (for example if Tabata method used)
 * @param numberOfRepetitions for example if Tabata method used
 * @param distance if relevant, to be expressed in meters
 */
public record Exercise(
    UUID uuid,
    String name,
    String category,
    Integer duration,
    Integer numberOfSeries,
    Integer restDurationBetweenSeries,
    Integer numberOfRepetitions,
    Integer distance
) implements IDataModel {
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        if (uuid != null) data.put("uuid", uuid);
        if (name != null) data.put("name", name);
        if (category != null) data.put("category", category);
        if (duration != null) data.put("duration", duration);
        if (numberOfSeries != null) data.put("numberOfSeries", numberOfSeries);
        if (restDurationBetweenSeries != null) data.put("restDurationBetweenSeries", restDurationBetweenSeries);
        if (numberOfRepetitions != null) data.put("numberOfRepetitions", numberOfRepetitions);
        if (distance != null) data.put("distance", distance);
        return data;
    }
}
