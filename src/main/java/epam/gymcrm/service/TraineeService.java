package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.*;

import java.util.List;

public interface TraineeService {

    void deleteByUsername(String username);

    List<TrainingDto> getTraineeTrainingsByUsername(String username);

    TraineeProfileResponseDto getByUsername(String username);

    UpdateTraineeProfileResponseDto updateProfile(UpdateTraineeProfileRequestDto updateTraineeDto);

    UpdateTraineeTrainersResponseDto updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto);

    void changeStatus(ActivateDeactivateRequestDto statusDto);
}
