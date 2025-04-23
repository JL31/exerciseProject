package com.jl31.exercise.usecases.getExercise;

import com.jl31.exercise.dataModels.Exercise;
import com.jl31.exercise.tools.AbstractUsecase;

import com.jl31.exercise.tools.IDataModel;
import com.jl31.exercise.tools.UsecaseRequestValidation;
import com.jl31.exercise.tools.ResourceNotFoundException;
import com.jl31.exercise.usecases.IExerciseServerSide;
import com.jl31.exercise.usecases.IUsecaseResponse;

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
    public UsecaseRequestValidation isValidRequest(IDataModel usecaseRequest) throws Exception {
        return new UsecaseRequestValidation(true, new HashMap<>());
    }

    @Override
    public IUsecaseResponse execute(IDataModel inputExercise) throws Exception {

        Optional<Exercise> fetchedExercise = this.serverSideAdapter.getExercise(inputExercise);

        return fetchedExercise
            .map(FetchedExercise::new)
            .orElseThrow(() -> {
                Exercise providedExercise = (Exercise) inputExercise;
                String errorMessage = "No exercise found with uuid : " + providedExercise.uuid() + " (may come from an unexpected error with database)";
                logger.error(errorMessage);
                return new ResourceNotFoundException(errorMessage);
            });

    }

}
