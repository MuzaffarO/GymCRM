package epam.gymcrm.service;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.model.Training;

import java.util.List;

public interface TrainingService {
    List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsRequest trainingsRequestDto);
    List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsRequest trainerTrainingsRequest);

    Training createTraining(TrainingRegister trainingRegister);
    Training cancelTraining(Integer trainingId);

}
