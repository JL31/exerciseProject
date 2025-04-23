package com.jl31.exercise.controller;

/**
 * Defines a generic API response without data
 */
public class ApiResponseWithoutData {
    private int status;
    private String message;

    /**
     * Class constructor to configure generic API response without data
     * @param status the HTTP response status code
     * @param message a message detailing what went well or wrong
     */
    public ApiResponseWithoutData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters et setters
    public String getMessage() {
        return message;
    }

}
