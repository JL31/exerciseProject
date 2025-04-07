package com.example.demo.controller;

public class ApiResponseWithoutData {
    private int status;
    private String message;

    public ApiResponseWithoutData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters et setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
