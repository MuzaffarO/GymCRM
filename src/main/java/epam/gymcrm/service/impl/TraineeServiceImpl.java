package epam.gymcrm.service.impl;

import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequestDto;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequestDto;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponseDto;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerResponseDto;
import epam.gymcrm.dto.user.request.SpecializationNameDto;
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

    @Override
    @Timed(value = "gymcrm.trainee.profile.get", description = "Time to fetch trainee profile")
    public TraineeProfileResponseDto getByUsername(String username) {
        Trainee trainee = getTraineeByUsername(username);
        return mapToProfileResponse(trainee);
    }

    @Override
    public UpdateTraineeProfileResponseDto updateProfile(UpdateTraineeProfileRequestDto updateTraineeProfileRequestDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeProfileRequestDto.getUsername());
        updateTraineeDetails(trainee, updateTraineeProfileRequestDto);
        traineeRepository.save(trainee);
        return mapToUpdateProfileResponse(trainee);
    }

    @Override
    public UpdateTraineeTrainersResponseDto updateTraineeTrainersList(UpdateTraineeTrainerListRequestDto updateTraineeTrainerListDto) {
        Trainee trainee = getTraineeByUsername(updateTraineeTrainerListDto.getUsername());

        List<String> trainerUsernames = updateTraineeTrainerListDto.getTrainersList().stream()
                .map(TrainerUsernameRequestDto::getUsername)
                .toList();
        setTraineeTrainersList(trainee, trainerUsernames);


        return new UpdateTraineeTrainersResponseDto(
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
        );
    }

    @Override
    public void changeStatus(ActivateDeactivateRequestDto statusDto) {
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
