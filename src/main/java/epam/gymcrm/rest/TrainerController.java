package epam.gymcrm.rest;

import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.TrainerTrainingsListResponseDto;
import epam.gymcrm.dto.response.UpdateTrainerProfileResponseDto;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.TrainingServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/trainers")
@RestController
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerServices trainerServices;
    private final TrainingServices trainingServices;

    @GetMapping("/by-username")
    public ResponseEntity<TrainerProfileResponseDto> getByUsername(@RequestParam("username") String username) {
        return trainerServices.getByUsername(username);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(@RequestBody @Valid UpdateTrainerProfileRequestDto updateTrainerProfileRequestDto) {
        return trainerServices.updateProfile(updateTrainerProfileRequestDto);
    }

    @GetMapping("/not-assigned-active")
    public ResponseEntity <List<TrainerResponseDto>> getNotAssignedActiveTrainers(@RequestParam("username") String username) {
        return trainerServices.getNotAssignedActiveTrainers(username);
    }

    @GetMapping("/trainings-list")
    public ResponseEntity<List<TrainerTrainingsListResponseDto>> getTrainerTrainings(@RequestBody @Valid TrainerTrainingsRequestDto trainerTrainingsRequestDto) {
        return trainingServices.getTrainerTrainings(trainerTrainingsRequestDto);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(@RequestBody @Valid ActivateDeactivateRequestDto statusDto){
        return trainerServices.changeStatus(statusDto);
    }

}
