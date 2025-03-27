package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.UpdateTrainerProfileResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainerServices {

    List<TrainingDto> getTrainerTrainingsByUsername(String username);

    ResponseEntity<TrainerProfileResponseDto> getByUsername(String username);

    ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(UpdateTrainerProfileRequestDto updateTrainerProfileDto);

    ResponseEntity<List<TrainerResponseDto>> getNotAssignedActiveTrainers(String username);

    ResponseEntity<Void> changeStatus(ActivateDeactivateRequestDto statusDto);
}
