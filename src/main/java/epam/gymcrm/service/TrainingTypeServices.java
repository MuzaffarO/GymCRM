package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingTypeServices {
    ResponseEntity<List<TrainingTypeDto>> getTrainingType();

    ResponseEntity<TrainingType> createTrainingType(String name);
}
