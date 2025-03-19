package epam.gymcrm.service;

import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.UpdateTraineeDto;
import epam.gymcrm.dto.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.response.UpdateTraineeProfileResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TraineeServices extends CRUDServices<TraineeDto, Integer> {

    ResponseEntity<Void> deleteByUsername(String username);

    List<TrainingDto> getTraineeTrainingsByUsername(String username);

    ResponseEntity<TraineeProfileResponseDto> getByUsername(String username);

    ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(UpdateTraineeDto updateTraineeDto);
}
