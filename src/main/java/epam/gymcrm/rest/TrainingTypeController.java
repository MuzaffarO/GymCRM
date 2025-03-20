package epam.gymcrm.rest;

import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.service.TrainingTypeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/training-type")
@RestController
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeServices trainingTypeServices;

    @GetMapping
    public ResponseEntity<List<TrainingTypeDto>> getTrainingType() {
        return trainingTypeServices.getTrainingType();
    }
}
