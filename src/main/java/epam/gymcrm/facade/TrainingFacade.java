package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponseDto;
import epam.gymcrm.dto.training.request.TrainingRegisterDto;
import epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingFacade {

    private final TrainingService trainingService;

    public void createTraining(TrainingRegisterDto dto) {
        trainingService.createTraining(dto);
    }

    public List<TraineeTrainingsListResponseDto> getTraineeTrainings(TraineeTrainingsRequestDto dto) {
        return trainingService.getTraineeTrainings(dto);
    }

    public List<TrainerTrainingsListResponseDto> getTrainerTrainings(TrainerTrainingsRequestDto dto) {
        return trainingService.getTrainerTrainings(dto);
    }

}
