package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponseDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TraineeFacade {

    private final TraineeService traineeService;
    private final TrainingService trainingService;

    public TraineeProfileResponseDto getByUsername(String username) {
        return traineeService.getByUsername(username);
    }

    public UpdateTraineeProfileResponseDto updateProfile(UpdateTraineeProfileRequestDto dto) {
        return traineeService.updateProfile(dto);
    }

    public void deleteByUsername(String username) {
        traineeService.deleteByUsername(username);
    }

    public UpdateTraineeTrainersResponseDto updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto dto) {
        return traineeService.updateTraineeTrainersList(dto);
    }

    public List<TraineeTrainingsListResponseDto> getTrainingsList(TraineeTrainingsRequestDto dto) {
        return trainingService.getTraineeTrainings(dto);
    }

    public void changeStatus(ActivateDeactivateRequestDto dto) {
        traineeService.changeStatus(dto);
    }
}
