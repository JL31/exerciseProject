package com.jl31.exercise.usecases.createExercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jl31.exercise.usecases.IExerciseServerSide;
import com.jl31.exercise.usecases.IUsecaseResponse;

import com.jl31.exercise.tools.IDataModel;
import com.jl31.exercise.tools.AbstractUsecase;
import com.jl31.exercise.tools.UsecaseRequestValidation;

import com.jl31.exercise.dataModels.Exercise;

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
    public UsecaseRequestValidation isValidRequest(IDataModel usecaseRequest) throws IllegalArgumentException {
        if (!(usecaseRequest instanceof Exercise exercise)) {
            throw new IllegalArgumentException("Incorrect usecase request type");
        }

        boolean validity = true;
        Map<String, String> invalidParameters = new HashMap<>();

        Matcher nameMatcher = nameRegexPattern.matcher(exercise.name());
        Matcher categoryMatcher = categoryRegexPattern.matcher(exercise.category());

        if (!nameMatcher.find()) {
            invalidParameters.put("name", exercise.name());
            validity = false;
        }

        if (!categoryMatcher.find()) {
            invalidParameters.put("category", exercise.category());
            validity = false;
        }

        return new UsecaseRequestValidation(validity, invalidParameters);
    }

    @Override
    public IUsecaseResponse execute(IDataModel usecaseRequest) throws Exception {

        Gson gson = new Gson();

        UsecaseRequestValidation requestValidation = isValidRequest(usecaseRequest);

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

        Optional<Exercise> createdExercise = this.serverSideAdapter.addExercise(usecaseRequest);

        return createdExercise
            .map(CreatedExercise::new)
            .orElseThrow(() -> {
                String msg = "Issue during exercise insertion into database";
                logger.error(msg);
                return new IllegalArgumentException(msg);
            });

    }

}
