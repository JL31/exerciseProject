package com.jl31.exercise.tools;

import com.jl31.exercise.usecases.IUsecaseResponse;

/**
 * Abstract class for every usecase
 */
public abstract class AbstractUsecase {

    /**
     * Contains the logic to validate a usecase request
     * @param usecaseRequest the usecases request to be validated
     * @return the results of the usecase request validation (including parameters in error if some)
     * @throws Exception to handle exceptions if needed (for example if type mismatch)
     */
    public abstract UsecaseRequestValidation isValidRequest(IDataModel usecaseRequest) throws Exception;

    /**
     * Core usecase method that contains the business logic
     * @param usecaseRequest all relevant data to serve implemented business logic
     * @return a specific response
     * @throws Exception to handle exceptions if needed
     */
    public abstract IUsecaseResponse execute(IDataModel usecaseRequest) throws Exception;
}
