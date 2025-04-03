package epam.gymcrm.service;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeDTO> getTrainingType();

    epam.gymcrm.model.TrainingType createTrainingType(String name);
}
