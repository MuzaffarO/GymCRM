package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.*;
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

import java.util.*;

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
    public ResponseEntity<UpdateTraineeProfileResponseDto> updateProfile(UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeProfileRequestDto.getUsername());
        updateTraineeDetails(trainee, updateTraineeProfileRequestDto);
        traineeDao.update(trainee);
        return ResponseEntity.ok(mapToUpdateProfileResponse(trainee));
    }


    // NEED TO IMPLEMENT THIS METHOD // ISSUE WITH MERGE
    @Override
    public ResponseEntity<TrainerResponseDto> updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeTrainerListDto.getUsername());

        List<Trainer> newTrainers = traineeDao.findAllTrainersByUsernameList(updateTraineeTrainerListDto.getTrainersList());

        trainee.getTrainers().clear();
        trainee.getTrainers().addAll(newTrainers);

        traineeDao.update(trainee);

        return ResponseEntity.ok(new TrainerResponseDto(
                trainee.getUser().getUsername(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getTrainers().stream()
                        .findFirst().flatMap(trainer -> Optional.ofNullable(trainer.getSpecializationType())
                                .map(specialization -> new SpecializationNameDto(specialization.getTrainingTypeName())))
                        .orElse(null)
        ));
    }

    @Override
    public ResponseEntity<Void> changeStatus(ActivateDeactivateRequestDto statusDto) {
        Trainee trainee = getTraineeByUsername(statusDto.getUsername());

        try {
            if (trainee.getUser().isActive() != statusDto.getIsActive()) {
                trainee.getUser().setActive(statusDto.getIsActive());
                traineeDao.update(trainee);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating trainee status");
        }

        return ResponseEntity.ok().build();
    }



    private Trainee getTraineeByUsername(String username) {
        return traineeDao.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found for username: " + username));
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

    private void updateTraineeDetails(Trainee trainee, UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        User user = trainee.getUser();

        user.setFirstName(Optional.ofNullable(updateTraineeProfileRequestDto.getFirstName()).orElse(user.getFirstName()));
        user.setLastName(Optional.ofNullable(updateTraineeProfileRequestDto.getLastName()).orElse(user.getLastName()));
        user.setActive(updateTraineeProfileRequestDto.getIsActive());

        trainee.setAddress(Optional.ofNullable(updateTraineeProfileRequestDto.getAddress()).orElse(trainee.getAddress()));
        trainee.setDateOfBirth(Optional.ofNullable(updateTraineeProfileRequestDto.getDateOfBirth()).orElse(trainee.getDateOfBirth()));
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
                                .map(specialization -> new SpecializationNameDto(specialization.getTrainingTypeName()))
                                .orElse(null)
                ))
                .toList();
    }
}
