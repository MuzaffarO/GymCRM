package epam.gymcrm.facade;

import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponseDto;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponseDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
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

    public TrainerProfileResponseDto getByUsername(String username) {
        return trainerService.getByUsername(username);
    }

    public UpdateTrainerProfileResponseDto updateProfile(UpdateTrainerProfileRequestDto dto) {
        return trainerService.updateProfile(dto);
    }

    public List<TrainerResponseDto> getNotAssignedActiveTrainers(String username) {
        return trainerService.getNotAssignedActiveTrainers(username);
    }

    public List<TrainerTrainingsListResponseDto> getTrainerTrainings(TrainerTrainingsRequestDto dto) {
        return trainingService.getTrainerTrainings(dto);
    }

    public void changeStatus(ActivateDeactivateRequestDto dto) {
        trainerService.changeStatus(dto);
    }
}
