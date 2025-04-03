package epam.gymcrm.service;

import epam.gymcrm.dto.training.TrainingDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerResponseDto;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponseDto;

import java.util.List;

public interface TrainerService {

    List<TrainingDto> getTrainerTrainingsByUsername(String username);

    TrainerProfileResponseDto getByUsername(String username);

    UpdateTrainerProfileResponseDto updateProfile(UpdateTrainerProfileRequestDto updateTrainerProfileDto);

    List<TrainerResponseDto> getNotAssignedActiveTrainers(String username);

    void changeStatus(ActivateDeactivateRequestDto statusDto);
}
