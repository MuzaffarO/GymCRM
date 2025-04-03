package epam.gymcrm.service.impl;

import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequest;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponse;
import epam.gymcrm.dto.trainer.response.TrainerResponse;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.mapper.TraineeMapper;
import epam.gymcrm.mapper.TrainingMapper;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeMapper traineeMapper;

    @Override
    @Timed(value = "gymcrm.trainee.profile.get", description = "Time to fetch trainee profile")
    public TraineeProfileResponse getByUsername(String username) {
        Trainee trainee = getTraineeByUsername(username);
        return mapToProfileResponse(trainee);
    }

    @Override
    public UpdateTraineeProfileResponse updateProfile(UpdateTraineeProfileRequest updateTraineeProfileRequest) {
        Trainee trainee = getTraineeByUsername(updateTraineeProfileRequest.getUsername());
        updateTraineeDetails(trainee, updateTraineeProfileRequest);
        traineeRepository.save(trainee);
        return mapToUpdateProfileResponse(trainee);
    }

    @Override
    public UpdateTraineeTrainersResponse updateTraineeTrainersList(UpdateTraineeTrainerListRequest updateTraineeTrainerListDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeTrainerListDto.getUsername());

        List<String> trainerUsernames = updateTraineeTrainerListDto.getTrainersList().stream()
                .map(TrainerUsernameRequest::getUsername)
                .toList();
        setTraineeTrainersList(trainee, trainerUsernames);


        return new UpdateTraineeTrainersResponse(
                trainee.getTrainers().stream()
                        .map(trainer -> new TrainerResponse(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                Optional.ofNullable(trainer.getSpecializationType())
                                        .map(specialization -> new SpecializationName(specialization.getTrainingTypeName()))
                                        .orElse(null)
                        ))
                        .toList()
        );
    }

    @Override
    public void changeStatus(ActivateDeactivateRequest statusDto) {
        Trainee trainee = getTraineeByUsername(statusDto.getUsername());

        try {
            if (trainee.getUser().isActive() != statusDto.getIsActive()) {
                trainee.getUser().setActive(statusDto.getIsActive());
                traineeRepository.save(trainee);
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while updating trainee status");
        }
    }

    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = getTraineeByUsername(username);
        try {
            traineeRepository.delete(trainee);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error deleting trainee with username: " + username);
        }
    }


    private Trainee getTraineeByUsername(String username) {
        return traineeRepository.findByUserUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Trainee not found for username: " + username));
    }

    private void updateTraineeDetails(Trainee trainee, UpdateTraineeProfileRequest updateTraineeProfileRequest) {
        User user = trainee.getUser();

        user.setFirstName(Optional.ofNullable(updateTraineeProfileRequest.getFirstName()).orElse(user.getFirstName()));
        user.setLastName(Optional.ofNullable(updateTraineeProfileRequest.getLastName()).orElse(user.getLastName()));
        user.setActive(updateTraineeProfileRequest.getIsActive());

        trainee.setAddress(Optional.ofNullable(updateTraineeProfileRequest.getAddress()).orElse(trainee.getAddress()));
        trainee.setDateOfBirth(Optional.ofNullable(updateTraineeProfileRequest.getDateOfBirth()).orElse(trainee.getDateOfBirth()));
    }

    private TraineeProfileResponse mapToProfileResponse(Trainee trainee) {
        return new TraineeProfileResponse(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                mapTrainers(trainee.getTrainers())
        );
    }

    private UpdateTraineeProfileResponse mapToUpdateProfileResponse(Trainee trainee) {
        return new UpdateTraineeProfileResponse(
                trainee.getUser().getUsername(),
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                mapTrainers(trainee.getTrainers())
        );
    }

    private List<TrainerResponse> mapTrainers(List<Trainer> trainers) {
        return trainers.stream()
                .map(trainer -> new TrainerResponse(
                        trainer.getUser().getUsername(),
                        trainer.getUser().getFirstName(),
                        trainer.getUser().getLastName(),
                        Optional.ofNullable(trainer.getSpecializationType())
                                .map(specialization -> new SpecializationName(specialization.getTrainingTypeName()))
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
