package epam.gymcrm.service.impl;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.mapper.TrainingMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServicesImpl implements TrainerServices {

    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;

    @Override
    public List<TrainingDto> getTrainerTrainingsByUsername(String username) {
        try {
            return trainerRepository.getTrainerTrainingsByUsername(username)
                    .stream()
                    .map(trainingMapper::toDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error fetching trainings for username: " + username);
        }
    }

    @Override
    @Timed(value = "gymcrm.trainer.profile.get", description = "Time to fetch trainer profile")
    public ResponseEntity<TrainerProfileResponseDto> getByUsername(String username) {
        Trainer trainer = getTrainerByUsername(username);
        return ResponseEntity.ok(mapToProfileResponse(trainer));
    }

    @Override
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(UpdateTrainerProfileRequestDto requestDto) {
        Trainer trainer = getTrainerByUsername(requestDto.getUsername());
        updateTrainerDetails(trainer, requestDto);
        trainerRepository.save(trainer);  // Using repository to save
        return ResponseEntity.ok(mapToUpdateProfileResponse(trainer));
    }

    @Override
    public ResponseEntity<List<TrainerResponseDto>> getNotAssignedActiveTrainers(String username) {
        traineeRepository.findByUserUsername(username).orElseThrow(() -> new UserNotFoundException("Trainee not found"));

        try {
            List<TrainerResponseDto> responseDtoList = trainerRepository.findNotAssignedActiveTrainers(username)
                    .stream()
                    .map(trainer -> new TrainerResponseDto(
                            trainer.getUser().getUsername(),
                            trainer.getUser().getFirstName(),
                            trainer.getUser().getLastName(),
                            new SpecializationNameDto(trainer.getSpecializationType().getTrainingTypeName())
                    ))
                    .toList();
            return ResponseEntity.ok(responseDtoList);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while getting the data!");
        }
    }

    @Override
    public ResponseEntity<Void> changeStatus(ActivateDeactivateRequestDto statusDto) {
        Trainer trainer = getTrainerByUsername(statusDto.getUsername());

        try {
            trainer.getUser().setActive(statusDto.getIsActive());
            trainerRepository.save(trainer);  // Using repository to update
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating trainer status");
        }

        return ResponseEntity.ok().build();
    }

    private Trainer getTrainerByUsername(String username) {
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainer not found for username: " + username));
    }

    private void updateTrainerDetails(Trainer trainer, UpdateTrainerProfileRequestDto requestDto) {
        User user = trainer.getUser();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setActive(requestDto.getIsActive());
    }

    private TrainerProfileResponseDto mapToProfileResponse(Trainer trainer) {
        return new TrainerProfileResponseDto(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                Optional.ofNullable(trainer.getSpecializationType())
                        .map(specialization -> new SpecializationNameDto(specialization.getTrainingTypeName()))
                        .orElse(null),
                trainer.getUser().isActive(),
                mapTrainees(trainer)
        );
    }

    private UpdateTrainerProfileResponseDto mapToUpdateProfileResponse(Trainer trainer) {
        return new UpdateTrainerProfileResponseDto(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                Optional.ofNullable(trainer.getSpecializationType())
                        .map(specialization -> new SpecializationNameDto(specialization.getTrainingTypeName()))
                        .orElse(null),
                trainer.getUser().isActive(),
                mapTrainees(trainer)
        );
    }

    private List<TraineeResponseDto> mapTrainees(Trainer trainer) {
        return trainer.getTrainees().stream()
                .map(trainee -> new TraineeResponseDto(
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getUser().getUsername()
                ))
                .toList();
    }
}
