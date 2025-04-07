package com.example.demo.tools;

import com.example.demo.usecases.IUsecaseResponse;

import java.util.Optional;

public abstract class AbstractUsecase {

    public abstract RequestValidation isValidRequest(IDataModel usecaseRequest) throws Exception;

    public abstract Optional<IUsecaseResponse> execute(IDataModel usecaseRequest) throws Exception;
}
