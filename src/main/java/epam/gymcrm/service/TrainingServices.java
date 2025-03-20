package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.response.TrainerTrainingsListResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingServices extends CRUDServices<TrainingDto, Integer> {
    ResponseEntity<List<TraineeTrainingsListResponseDto>> getTraineeTrainings(TraineeTrainingsRequestDto trainingsRequestDto);
    ResponseEntity<List<TrainerTrainingsListResponseDto>> getTrainerTrainings(TrainerTrainingsRequestDto trainerTrainingsRequestDto);

    ResponseEntity<Void> createTraining(TrainingRegisterDto trainingRegisterDto);
}
