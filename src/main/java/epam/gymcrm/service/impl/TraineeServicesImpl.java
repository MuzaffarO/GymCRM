package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.UpdateTraineeDto;
import epam.gymcrm.dto.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.response.TrainerResponseDto;
import epam.gymcrm.dto.response.TrainingTypeResponseDto;
import epam.gymcrm.dto.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.mapper.TraineeMapper;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TraineeServicesImpl extends AbstractCrudServicesImpl<Trainee, TraineeDto, Integer> implements TraineeServices {

    private final TraineeDao traineeDao;
    private final TrainingMapper trainingMapper;

    public TraineeServicesImpl(TraineeDao traineeDao, TraineeMapper mapper, TrainingMapper trainingMapper) {
        super(traineeDao, mapper);
        this.traineeDao = traineeDao;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public List<TrainingDto> getTraineeTrainingsByUsername(String username) {
        try {
            return traineeDao.getTraineeTrainingsByUsername(username)
                    .stream()
                    .map(trainingMapper::toDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error fetching trainings for username: " + username);
        }
    }

    @Override
    public ResponseEntity<TraineeProfileResponseDto> getByUsername(String username) {
        Trainee trainee = getTraineeByUsername(username);
        return ResponseEntity.ok(mapToProfileResponse(trainee));
    }

    @Override
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(UpdateTraineeDto updateTraineeDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeDto.getUsername());
        updateTraineeDetails(trainee, updateTraineeDto);
        traineeDao.update(trainee);
        return ResponseEntity.ok(mapToUpdateProfileResponse(trainee));
    }

    @Override
    public ResponseEntity<Void> deleteByUsername(String username) {
        getTraineeByUsername(username); // this is to check if the trainee exists or not
        try {
            traineeDao.deleteByUsername(username);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error deleting trainee with username: " + username);
        }
        return ResponseEntity.ok().build();
    }

    private Trainee getTraineeByUsername(String username) {
        return traineeDao.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found for username: " + username));
    }

    private void updateTraineeDetails(Trainee trainee, UpdateTraineeDto updateTraineeDto) {
        User user = trainee.getUser();

        user.setFirstName(Optional.ofNullable(updateTraineeDto.getFirstName()).orElse(user.getFirstName()));
        user.setLastName(Optional.ofNullable(updateTraineeDto.getLastName()).orElse(user.getLastName()));
        user.setActive(updateTraineeDto.getIsActive());

        trainee.setAddress(Optional.ofNullable(updateTraineeDto.getAddress()).orElse(trainee.getAddress()));
        trainee.setDateOfBirth(Optional.ofNullable(updateTraineeDto.getDateOfBirth()).orElse(trainee.getDateOfBirth()));
    }


    private TraineeProfileResponseDto mapToProfileResponse(Trainee trainee) {
        return new TraineeProfileResponseDto(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                mapTrainers(trainee.getTrainers())
        );
    }

    private UpdateTraineeProfileResponseDto mapToUpdateProfileResponse(Trainee trainee) {
        return new UpdateTraineeProfileResponseDto(
                trainee.getUser().getUsername(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                mapTrainers(trainee.getTrainers())
        );
    }

    private List<TrainerResponseDto> mapTrainers(List<Trainer> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerResponseDto(
                        trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        Optional.ofNullable(trainer.getSpecializationType())
                                .map(specialization -> new TrainingTypeResponseDto(specialization.getTrainingTypeName()))
                                .orElse(null)
                ))
                .toList();
    }
}
