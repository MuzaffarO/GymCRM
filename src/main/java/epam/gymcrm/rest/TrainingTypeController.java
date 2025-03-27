package epam.gymcrm.rest;

import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.TrainingTypeServices;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<TrainingType> createTrainingType(@NotEmpty @RequestParam String name) {
        return trainingTypeServices.createTrainingType(name);
    }
}
