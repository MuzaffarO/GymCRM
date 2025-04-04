package epam.gymcrm.service;

import epam.gymcrm.dto.trainingtype.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeDto> getTrainingType();

    TrainingType createTrainingType(String name);
}
