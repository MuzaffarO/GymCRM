package epam.gymcrm.facade;

import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponse;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.service.TrainerService;
import epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainerFacade {

    private final TrainerService trainerService;
    private final TrainingService trainingService;

    public TrainerProfileResponse getByUsername(String username) {
        return trainerService.getByUsername(username);
    }

    public UpdateTrainerProfileResponse updateProfile(UpdateTrainerProfileRequest dto) {
        return trainerService.updateProfile(dto);
    }

    public List<TrainerResponse> getNotAssignedActiveTrainers(String username) {
        return trainerService.getNotAssignedActiveTrainers(username);
    }

    public List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsRequest dto) {
        return trainingService.getTrainerTrainings(dto);
    }

    public void changeStatus(ActivateDeactivateRequest dto) {
        trainerService.changeStatus(dto);
    }
}
