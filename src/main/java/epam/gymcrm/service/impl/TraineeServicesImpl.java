package epam.gymcrm.service.impl;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TrainerUsernameRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.mapper.TraineeMapper;
import epam.gymcrm.service.mapper.TrainingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TraineeServicesImpl implements TraineeServices {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeMapper traineeMapper;

    @Override
    public List<TrainingDto> getTraineeTrainingsByUsername(String username) {
        try {
            return traineeRepository.findByUserUsername(username)
                    .map(trainee -> trainee.getTrainings().stream()
                            .map(trainingMapper::toDto)
                            .toList())
                    .orElseThrow(() -> new UserNotFoundException("Trainee not found with username: " + username));
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
        traineeRepository.save(trainee);
        return ResponseEntity.ok(mapToUpdateProfileResponse(trainee));
    }

    @Override
    public ResponseEntity<UpdateTraineeTrainersResponseDto> updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeTrainerListDto.getUsername());

        List<String> trainerUsernames = updateTraineeTrainerListDto.getTrainersList().stream()
                .map(TrainerUsernameRequestDto::getUsername)
                .toList();
        setTraineeTrainersList(trainee, trainerUsernames);


        return ResponseEntity.ok(new UpdateTraineeTrainersResponseDto(
                trainee.getTrainers().stream()
                        .map(trainer -> new TrainerResponseDto(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                Optional.ofNullable(trainer.getSpecializationType())
                                        .map(specialization -> new SpecializationNameDto(specialization.getTrainingTypeName()))
                                        .orElse(null)
                        ))
                        .toList()
        ));
    }

    @Override
    public ResponseEntity<Void> changeStatus(ActivateDeactivateRequestDto statusDto) {
        Trainee trainee = getTraineeByUsername(statusDto.getUsername());

        try {
            if (trainee.getUser().isActive() != statusDto.getIsActive()) {
                trainee.getUser().setActive(statusDto.getIsActive());
                traineeRepository.save(trainee);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating trainee status");
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteByUsername(String username) {
        Trainee trainee = getTraineeByUsername(username);
        try {
            traineeRepository.delete(trainee);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error deleting trainee with username: " + username);
        }
        return ResponseEntity.ok().build();
    }


    private Trainee getTraineeByUsername(String username) {
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found for username: " + username));
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

    private void setTraineeTrainersList(Trainee trainee, List<String> trainerUsernames) {
        List<Trainer> trainers = trainerRepository.findAllByUsername(trainerUsernames);
        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);
    }
}
