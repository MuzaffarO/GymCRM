package epam.gymcrm.service;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.training.request.TrainingRegisterDto;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponseDto;

import java.util.List;

public interface TrainingService {
    List<TraineeTrainingsListResponseDto> getTraineeTrainings(TraineeTrainingsRequestDto trainingsRequestDto);
    List<TrainerTrainingsListResponseDto> getTrainerTrainings(TrainerTrainingsRequestDto trainerTrainingsRequestDto);

    void createTraining(TrainingRegisterDto trainingRegisterDto);
}
