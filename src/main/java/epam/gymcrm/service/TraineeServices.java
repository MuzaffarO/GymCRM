package epam.gymcrm.service;

import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.dto.TrainingDto;

import java.util.List;

public interface TraineeServices extends CRUDServices<TraineeDto, Integer> {

    void deleteByUsername(String username, String password);

    List<TrainingDto> getTraineeTrainingsByUsername(String username, String password);
}
