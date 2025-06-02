package epam.gymcrm.service;

import epam.gymcrm.dto.training.TrainingDTO;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponse;

import java.util.List;

public interface TrainerService {

    List<TrainingDTO> getTrainerTrainingsByUsername(String username);

    TrainerProfileResponse getByUsername(String username);

    UpdateTrainerProfileResponse updateProfile(UpdateTrainerProfileRequest updateTrainerProfileDto);

    List<TrainerResponse> getNotAssignedActiveTrainers(String username);

    void changeStatus(ActivateDeactivateRequest statusDto);
}
