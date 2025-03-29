package epam.gymcrm.service;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TraineeServices{

    ResponseEntity<Void> deleteByUsername(String username);

    List<TrainingDto> getTraineeTrainingsByUsername(String username);

    ResponseEntity<TraineeProfileResponseDto> getByUsername(String username);

    ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(UpdateTraineeProfileRequestDto updateTraineeDto);

    ResponseEntity<UpdateTraineeTrainersResponseDto> updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto);

    ResponseEntity<Void> changeStatus(ActivateDeactivateRequestDto statusDto);
}
