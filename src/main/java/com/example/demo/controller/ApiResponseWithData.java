package com.example.demo.controller;

public class ApiResponseWithData extends ApiResponseWithoutData {
    private Object data;

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
