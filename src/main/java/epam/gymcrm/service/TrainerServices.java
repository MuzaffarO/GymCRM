package epam.gymcrm.service;

import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.dto.TrainingDto;

import java.util.List;

public interface TrainerServices extends CRUDServices<TrainerDto, Integer> {

    List<TrainingDto> getTrainerTrainingsByUsername(String username);
}
