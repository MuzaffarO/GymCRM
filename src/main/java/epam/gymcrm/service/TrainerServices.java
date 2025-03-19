package epam.gymcrm.service;

import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.UpdateTrainerProfileResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TrainerServices extends CRUDServices<TrainerDto, Integer> {

    List<TrainingDto> getTrainerTrainingsByUsername(String username);

    ResponseEntity<TrainerProfileResponseDto> getByUsername(String username);

    ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(UpdateTrainerProfileRequestDto updateTrainerProfileDto);

    ResponseEntity<List<TrainerResponseDto>> getNotAssignedActiveTrainers(String username);
}
