package com.example.demo.usecases.createExercise;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.tools.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.usecases.IExerciseServerSide;
import com.example.demo.usecases.IUsecaseResponse;

import com.example.demo.tools.IDataModel;
import com.example.demo.tools.AbstractUsecase;
import com.example.demo.tools.RequestValidation;
import com.example.demo.dataModels.BaseExercise;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Optional;

import java.text.MessageFormat;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;


@Service
public class CreateExerciseUsecase extends AbstractUsecase {

    private static final Logger logger = LoggerFactory.getLogger(CreateExerciseUsecase.class);

    private final String nameRegex = "^[A-Za-z ]{0,100}$";
    private final String categoryRegex = "^[A-Za-z ]{0,50}$";

    private final Pattern nameRegexPattern = Pattern.compile(nameRegex);
    private final Pattern categoryRegexPattern = Pattern.compile(categoryRegex);

    IExerciseServerSide serverSideAdapter;

    public CreateExerciseUsecase (IExerciseServerSide serverSideAdapter) {
        this.serverSideAdapter = serverSideAdapter;
    }

    @Override
    public RequestValidation isValidRequest(IDataModel usecaseRequest) throws IllegalArgumentException {
        if (!(usecaseRequest instanceof BaseExercise baseExercise)) {
            throw new IllegalArgumentException("Incorrect usecase request type");
        }

        boolean validity = true;
        Map<String, String> invalidParameters = new HashMap<String, String>();

        Matcher nameMatcher = nameRegexPattern.matcher(baseExercise.name());
        Matcher categoryMatcher = categoryRegexPattern.matcher(baseExercise.category());

        if (!nameMatcher.find()) {
            invalidParameters.put("name", baseExercise.name());
            validity = false;
        }

        if (!categoryMatcher.find()) {
            invalidParameters.put("category", baseExercise.category());
            validity = false;
        }

        return new RequestValidation(validity, invalidParameters);
    }

    @Override
    public Optional<IUsecaseResponse> execute(IDataModel usecaseRequest) throws Exception {

        Gson gson = new Gson();

        RequestValidation requestValidation = isValidRequest(usecaseRequest);

        if (!requestValidation.getRequestValidity()) {
            String jsonData = gson.toJson(requestValidation.getInvalidParameters());
            String logMessage = MessageFormat.format("Invalid usecase request with following data : {0}", jsonData);
            logger.error(logMessage);
            throw new IllegalArgumentException(logMessage);
        }

        Map<String, Object> usecaseRequestData = usecaseRequest.getData();
        String usecaseRequestDataAsJson = gson.toJson(usecaseRequestData);
        String logMessage = MessageFormat.format("Creation of a usecases exercise with the following data : {0}", usecaseRequestDataAsJson);
        logger.info(logMessage);

        Optional<CreatedExercise> createdExercise = this.serverSideAdapter.addExercise(usecaseRequest);

        if (createdExercise.isEmpty()) {
            String errorMessage = "Issue during exercise insertion into database";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } else {
            return createdExercise.map(exercise -> (IUsecaseResponse) exercise);
        }

    }

}
