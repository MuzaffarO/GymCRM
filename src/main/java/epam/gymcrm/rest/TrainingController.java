package epam.gymcrm.rest;

import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.service.TrainingServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/trainings")
@RestController
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingServices trainingServices;
    @PostMapping("/create")
    public ResponseEntity<Void> createTraining(@RequestBody TrainingRegisterDto trainingRegisterDto) {
        return trainingServices.createTraining(trainingRegisterDto);
    }
}
