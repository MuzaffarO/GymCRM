package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.mapper.TrainerMapper;
import epam.gymcrm.service.mapper.TrainingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServicesImpl extends AbstractCrudServicesImpl<Trainer, TrainerDto, Integer> implements TrainerServices {

    private final TrainerDao trainerDao;
    private final TrainingMapper trainingMapper;
    private final TraineeServices traineeServices;

    public TrainerServicesImpl(TrainerDao trainerDao, TrainerMapper mapper, TrainingMapper trainingMapper, TraineeServices traineeServices, TraineeServices traineeServices1) {
        super(trainerDao, mapper);
        this.trainerDao = trainerDao;
        this.trainingMapper = trainingMapper;
        this.traineeServices = traineeServices1;
    }

    @Override
    public List<TrainingDto> getTrainerTrainingsByUsername(String username) {
        try {
            return trainerDao.getTrainerTrainingsByUsername(username)
                    .stream()
                    .map(trainingMapper::toDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException("Error fetching trainings for username: " + username);
        }
    }

    @Override
    public ResponseEntity<TrainerProfileResponseDto> getByUsername(String username) {
        Trainer trainer = getTrainerByUsername(username);
        return ResponseEntity.ok(mapToProfileResponse(trainer));
    }

    @Override
    public ResponseEntity<UpdateTrainerProfileResponseDto> updateProfile(UpdateTrainerProfileRequestDto requestDto) {
        Trainer trainer = getTrainerByUsername(requestDto.getUsername());
        updateTrainerDetails(trainer, requestDto);
        trainerDao.update(trainer);
        return ResponseEntity.ok(mapToUpdateProfileResponse(trainer));
    }

    @Override
    public ResponseEntity<List<TrainerResponseDto>> getNotAssignedActiveTrainers(String username) {
        traineeServices.getByUsername(username); // checking if such trainee exists!

        try {
            List<TrainerResponseDto> responseDtoList = trainerDao.findNotAssignedActiveTrainers(username)
                    .stream()
                    .map(trainer -> new TrainerResponseDto(
                            trainer.getUser().getUsername(),
                            trainer.getUser().getFirstName(),
                            trainer.getUser().getLastName(),
                            new SpecializationNameDto(trainer.getSpecializationType().getTrainingTypeName())
                    )).toList();
            return ResponseEntity.ok(responseDtoList);
        } catch (DataAccessException e) {
            throw new DatabaseException("Error while getting the data!");
        }
    }

    private Trainer getTrainerByUsername(String username) {
        return trainerDao.findByUserUsername(username)
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
