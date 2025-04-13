package epam.gymcrm.service;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeDTO> getTrainingType();

    TrainingTypeDTO createTrainingType(String name);
}
