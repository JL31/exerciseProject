package com.jl31.exercise.tools;

import java.util.Map;

/**
 * Defines relevant data as regards usecase request validation
 */
public class UsecaseRequestValidation {
    public boolean requestValidity;
    public Map<String, String> invalidParameters;

    /**
     * Class constructor to define relevant data as regards usecase request validation
     * @param requestValidity enables to determine whether the usecase request is valid or not
     * @param invalidParameters if invalid usecase request contains the list of invalid parameters
     */
    public UsecaseRequestValidation(boolean requestValidity, Map<String, String> invalidParameters) {
        this.requestValidity = requestValidity;
        this.invalidParameters = invalidParameters;
    }

    // Getters
    public boolean getRequestValidity() {
        return requestValidity;
    }

    public Map<String, String> getInvalidParameters() {
        return invalidParameters;
    }
}
