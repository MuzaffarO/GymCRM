package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingServices extends CRUDServices<TrainingDto, Integer> {
    ResponseEntity<List<TraineeTrainingsListResponseDto>> getTrainingsList(TraineeTrainingsRequestDto trainingsRequestDto);
}
