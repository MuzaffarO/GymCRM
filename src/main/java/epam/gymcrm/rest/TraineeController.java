package epam.gymcrm.rest;

import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.TrainingServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeServices traineeServices;
    private final TrainingServices trainingServices;

    @GetMapping("/by-username")
    public ResponseEntity<TraineeProfileResponseDto> getByUsername(@RequestParam("username") @Valid String username) {
        return traineeServices.getByUsername(username);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(@RequestBody @Valid UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        return traineeServices.updateProfile(updateTraineeProfileRequestDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteByUsername(@RequestParam("username") @Valid String username) {
        traineeServices.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    // NEED TO FIX THIS METHOD

    @PutMapping("/update-trainers-list")
    public  ResponseEntity<TrainerResponseDto> updateTraineeTrainersList(@RequestBody @Valid UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto) {
        return traineeServices.updateTraineeTrainersList(updateTraineeTrainerListDto);
    }

    @GetMapping("/trainings-list")
    public ResponseEntity<List<TraineeTrainingsListResponseDto>> getTrainingsList(@RequestBody @Valid TraineeTrainingsRequestDto trainingsRequestDto) {
        return trainingServices.getTraineeTrainings(trainingsRequestDto);
    }

    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(@RequestBody @Valid ActivateDeactivateRequestDto statusDto){
        return traineeServices.changeStatus(statusDto);
    }

}
