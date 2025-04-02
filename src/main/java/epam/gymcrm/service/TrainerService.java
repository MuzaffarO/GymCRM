package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.UpdateTrainerProfileResponseDto;

import java.util.List;

public interface TrainerService {

    List<TrainingDto> getTrainerTrainingsByUsername(String username);

    TrainerProfileResponseDto getByUsername(String username);

    UpdateTrainerProfileResponseDto updateProfile(UpdateTrainerProfileRequestDto updateTrainerProfileDto);

    List<TrainerResponseDto> getNotAssignedActiveTrainers(String username);

    void changeStatus(ActivateDeactivateRequestDto statusDto);
}
