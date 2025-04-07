package com.example.demo.usecases.getExercise;

import com.example.demo.dataModels.ExerciseUuid;
import com.example.demo.tools.AbstractUsecase;

import com.example.demo.tools.IDataModel;
import com.example.demo.tools.RequestValidation;
import com.example.demo.tools.ResourceNotFoundException;
import com.example.demo.usecases.IExerciseServerSide;
import com.example.demo.usecases.IUsecaseResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;


@Service
public class GetExerciseUsecase extends AbstractUsecase {

    private static final Logger logger = LoggerFactory.getLogger(GetExerciseUsecase.class);

    IExerciseServerSide serverSideAdapter;

    public GetExerciseUsecase(IExerciseServerSide serverSideAdapter) {
        this.serverSideAdapter = serverSideAdapter;
    }

    @Override
    public RequestValidation isValidRequest(IDataModel usecaseRequest) throws Exception {
        return new RequestValidation(true, new HashMap<>());
    }

    @Override
    public Optional<IUsecaseResponse> execute(IDataModel usecaseExercise) throws Exception {

        Optional<FetchedExercise> fetchedExercise = this.serverSideAdapter.getExercise(usecaseExercise);

        if (fetchedExercise.isEmpty()) {
            ExerciseUuid providedExercise = (ExerciseUuid) usecaseExercise;
            String errorMessage = "No exercise found with uuid : " + providedExercise.uuid() + "(may come from an unexpected error with database)";
            logger.error(errorMessage);
            throw new ResourceNotFoundException(errorMessage);
        } else {
            return fetchedExercise.map(exercise -> (IUsecaseResponse) exercise);
        }

    }

}
