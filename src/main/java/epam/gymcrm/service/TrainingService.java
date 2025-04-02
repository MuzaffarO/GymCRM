package epam.gymcrm.service;

import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.response.TrainerTrainingsListResponseDto;

import java.util.List;

public interface TrainingService {
    List<TraineeTrainingsListResponseDto> getTraineeTrainings(TraineeTrainingsRequestDto trainingsRequestDto);
    List<TrainerTrainingsListResponseDto> getTrainerTrainings(TrainerTrainingsRequestDto trainerTrainingsRequestDto);

    void createTraining(TrainingRegisterDto trainingRegisterDto);
}
