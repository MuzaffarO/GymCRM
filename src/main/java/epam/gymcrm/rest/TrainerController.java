package epam.gymcrm.rest;

import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.response.UpdateTrainerProfileResponseDto;
import epam.gymcrm.service.TrainerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/trainers")
@RestController
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerServices trainerServices;

    @GetMapping("/by-username")
    public ResponseEntity<TrainerProfileResponseDto> getByUsername(@RequestParam("username") String username) {
        return trainerServices.getByUsername(username);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(@RequestBody UpdateTrainerProfileRequestDto updateTrainerProfileRequestDto) {
        return trainerServices.updateProfile(updateTrainerProfileRequestDto);
    }
}
