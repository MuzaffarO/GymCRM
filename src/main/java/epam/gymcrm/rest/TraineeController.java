package epam.gymcrm.rest;

import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.service.TraineeServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeServices traineeServices;

    @GetMapping("/by-username")
    public ResponseEntity<TraineeProfileResponseDto> getByUsername(@RequestParam("username") String username) {
        return traineeServices.getByUsername(username);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(@RequestBody UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        return traineeServices.updateProfile(updateTraineeProfileRequestDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteByUsername(@RequestParam("username") String username) {
        traineeServices.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }
}
