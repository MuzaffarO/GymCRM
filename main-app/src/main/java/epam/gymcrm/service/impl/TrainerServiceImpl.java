package epam.gymcrm.service.impl;

import epam.gymcrm.dto.training.TrainingDTO;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.trainee.response.TraineeResponse;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponse;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.service.TrainerService;
import epam.gymcrm.mapper.TrainingMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;

    @Override
    public List<TrainingDTO> getTrainerTrainingsByUsername(String username) {
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
    public TrainerProfileResponse getByUsername(String username) {
        Trainer trainer = getTrainerByUsername(username);
        return mapToProfileResponse(trainer);
    }

    @Override
    public UpdateTrainerProfileResponse updateProfile(UpdateTrainerProfileRequest requestDto) {
        Trainer trainer = getTrainerByUsername(requestDto.getUsername());
        updateTrainerDetails(trainer, requestDto);
        trainerRepository.save(trainer);  // Using repository to save
        return mapToUpdateProfileResponse(trainer);
    }

    @Override
    public List<TrainerResponse> getNotAssignedActiveTrainers(String username) {
        traineeRepository.findByUserUsername(username).orElseThrow(() -> new UserNotFoundException("Trainee not found"));

        try {
            List<TrainerResponse> responseDtoList = trainerRepository.findNotAssignedActiveTrainers(username)
                    .stream()
                    .map(trainer -> new TrainerResponse(
                            trainer.getUser().getUsername(),
                            trainer.getUser().getFirstName(),
                            trainer.getUser().getLastName(),
                            new SpecializationName(trainer.getSpecializationType().getTrainingTypeName())
                    ))
                    .toList();
            return responseDtoList;
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while getting the data!");
        }
    }

    @Override
    public void changeStatus(ActivateDeactivateRequest statusDto) {
        Trainer trainer = getTrainerByUsername(statusDto.getUsername());

        try {
            trainer.getUser().setActive(statusDto.getIsActive());
            trainerRepository.save(trainer);  // Using repository to update
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating trainer status");
        }
    }

    private Trainer getTrainerByUsername(String username) {
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainer not found for username: " + username));
    }

    private void updateTrainerDetails(Trainer trainer, UpdateTrainerProfileRequest requestDto) {
        User user = trainer.getUser();
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setActive(requestDto.getIsActive());
    }

    private TrainerProfileResponse mapToProfileResponse(Trainer trainer) {
        return new TrainerProfileResponse(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                Optional.ofNullable(trainer.getSpecializationType())
                        .map(specialization -> new SpecializationName(specialization.getTrainingTypeName()))
                        .orElse(null),
                trainer.getUser().isActive(),
                mapTrainees(trainer)
        );
    }

    private UpdateTrainerProfileResponse mapToUpdateProfileResponse(Trainer trainer) {
        return new UpdateTrainerProfileResponse(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                Optional.ofNullable(trainer.getSpecializationType())
                        .map(specialization -> new SpecializationName(specialization.getTrainingTypeName()))
                        .orElse(null),
                trainer.getUser().isActive(),
                mapTrainees(trainer)
        );
    }

    private List<TraineeResponse> mapTrainees(Trainer trainer) {
        return trainer.getTrainees().stream()
                .map(trainee -> new TraineeResponse(
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getUser().getUsername()
                ))
                .toList();
    }
}
