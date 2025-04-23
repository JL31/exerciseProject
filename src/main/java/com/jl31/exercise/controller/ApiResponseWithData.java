package com.jl31.exercise.controller;

/**
 * Defines a generic API response including data
 */
public class ApiResponseWithData extends ApiResponseWithoutData {
    private Object data;

    /**
     * Class constructor to configure generic API response including data
     * @param status the HTTP response status code
     * @param message a message detailing what went well or wrong
     * @param data the data to be returned
     */
    public ApiResponseWithData(int status, String message, Object data) {
        super(status, message);

        this.data = data;
    }

    // Getters et setters
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
