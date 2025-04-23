package com.jl31.exercise.tools;

/**
 * Specific exception to handle the case where a resource could not be found
 */
public class ResourceNotFoundException extends Exception {

    /**
     * Constructor for the resource not found specific exception
     * @param message Exception message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
