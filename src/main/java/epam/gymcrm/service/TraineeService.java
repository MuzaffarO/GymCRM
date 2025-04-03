package epam.gymcrm.service;

import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequest;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponse;


public interface TraineeService {

    void deleteByUsername(String username);

    TraineeProfileResponse getByUsername(String username);

    UpdateTraineeProfileResponse updateProfile(UpdateTraineeProfileRequest updateTraineeDto);

    UpdateTraineeTrainersResponse updateTraineeTrainersList(UpdateTraineeTrainerListRequest updateTraineeTrainerListDto);

    void changeStatus(ActivateDeactivateRequest statusDto);
}
