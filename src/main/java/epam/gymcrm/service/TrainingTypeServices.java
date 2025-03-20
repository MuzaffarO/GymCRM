package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingTypeDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainingTypeServices extends CRUDServices<TrainingTypeDto, Integer> {
    ResponseEntity<List<TrainingTypeDto>> getTrainingType();
}
