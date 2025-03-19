package epam.gymcrm.rest;

import epam.gymcrm.dto.response.TraineeProfileResponseDto;
import epam.gymcrm.service.TraineeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeServices traineeServices;

    @GetMapping("/by-username")
    public ResponseEntity<TraineeProfileResponseDto> getByUsername(@RequestParam("username") String username) {
        return traineeServices.getByUsername(username);
    }

}
