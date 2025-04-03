package epam.gymcrm.service;

import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponseDto;


public interface TraineeService {

    void deleteByUsername(String username);

    TraineeProfileResponseDto getByUsername(String username);

    UpdateTraineeProfileResponseDto updateProfile(UpdateTraineeProfileRequestDto updateTraineeDto);

    UpdateTraineeTrainersResponseDto updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto);

    void changeStatus(ActivateDeactivateRequestDto statusDto);
}
