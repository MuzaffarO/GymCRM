package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequest;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponse;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
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

    public void deleteByUsername(String username) {
        traineeService.deleteByUsername(username);
    }

    public TraineeProfileResponse getByUsername(String username) {
        return traineeService.getByUsername(username);
    }

    public UpdateTraineeProfileResponse updateProfile(UpdateTraineeProfileRequest dto) {
        return traineeService.updateProfile(dto);
    }

    public UpdateTraineeTrainersResponse updateTraineeTrainersList(UpdateTraineeTrainerListRequest dto) {
        return traineeService.updateTraineeTrainersList(dto);
    }

    public void changeStatus(ActivateDeactivateRequest dto) {
        traineeService.changeStatus(dto);
    }
    public List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsRequest dto) {
        return trainingService.getTraineeTrainings(dto);
    }


}
