package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingFacade {

    private final TrainingService trainingService;

    public void createTraining(TrainingRegister dto) {
        trainingService.createTraining(dto);
    }

    public List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsRequest dto) {
        return trainingService.getTraineeTrainings(dto);
    }

    public List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsRequest dto) {
        return trainingService.getTrainerTrainings(dto);
    }

}
