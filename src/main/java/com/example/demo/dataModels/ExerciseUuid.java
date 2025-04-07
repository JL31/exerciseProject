package com.example.demo.dataModels;

import com.example.demo.tools.IDataModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record ExerciseUuid (
    UUID uuid
) implements IDataModel {
    public Map<String, Object> getData() {
        return new HashMap<String, Object>(Map.of(
            "uuid", uuid
        ));
    }
};
