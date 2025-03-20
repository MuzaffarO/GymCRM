package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainerTrainingsRequestDto;
import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.dto.response.TrainerTrainingsListResponseDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.TrainingServices;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TrainingServicesImpl extends AbstractCrudServicesImpl<Training, TrainingDto, Integer> implements TrainingServices {

    private final TrainingDao trainingDao;
    private final TrainingMapper trainingMapper;
    private final TrainerDao trainerDao;
    private final TraineeDao traineeDao;
    private final TrainingTypeDao trainingTypeDao;

    public TrainingServicesImpl(TrainingDao trainingDao, TrainingMapper mapper, TrainerDao trainerDao, TraineeDao traineeDao, TrainingTypeDao trainingTypeDao) {
        super(trainingDao, mapper);
        this.trainingDao = trainingDao;
        this.trainingMapper = mapper;
        this.trainerDao = trainerDao;
        this.traineeDao = traineeDao;
        this.trainingTypeDao = trainingTypeDao;
    }

    @Override
    public ResponseEntity<Void> createTraining(TrainingRegisterDto trainingRegisterDto) {
        Trainee trainee = traineeDao.findByUserUsername(trainingRegisterDto.getTraineeUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainee not found with username: " + trainingRegisterDto.getTraineeUsername()));

        Trainer trainer = trainerDao.findByUserUsername(trainingRegisterDto.getTrainerUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainer not found with username: " + trainingRegisterDto.getTrainerUsername()));

        TrainingType trainingType = trainingTypeDao.findByName(trainingRegisterDto.getTrainingName())
                .orElseThrow(() -> new DatabaseException("Training type not found: " + trainingRegisterDto.getTrainingName()));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingName(trainingRegisterDto.getTrainingName());
        training.setTrainingDate(trainingRegisterDto.getTrainingDate());
        training.setTrainingDuration(trainingRegisterDto.getTrainingDuration());

        trainingDao.save(training);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TraineeTrainingsListResponseDto>> getTraineeTrainings(TraineeTrainingsRequestDto trainingsRequestDto) {
        traineeDao.getTraineeTrainingsByUsername(trainingsRequestDto.getUsername());

        List<Training> trainings = trainingDao.findTraineeTrainingsByFilters(
                trainingsRequestDto.getUsername(),
                trainingsRequestDto.getPeriodFrom(),
                trainingsRequestDto.getPeriodTo(),
                trainingsRequestDto.getTrainerName(),
                trainingsRequestDto.getTrainingType()
        );

        return ResponseEntity.ok(trainings.stream()
                .map(training -> new TraineeTrainingsListResponseDto(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName()
                ))
                .toList());
    }

    @Override
    public ResponseEntity<List<TrainerTrainingsListResponseDto>> getTrainerTrainings(TrainerTrainingsRequestDto trainerTrainingsRequestDto) {
        trainerDao.findByUserUsername(trainerTrainingsRequestDto.getUsername());

        List<Training> trainings = trainerDao.findTrainerTrainingsByFilters(
                trainerTrainingsRequestDto.getUsername(),
                trainerTrainingsRequestDto.getPeriodFrom(),
                trainerTrainingsRequestDto.getPeriodTo(),
                trainerTrainingsRequestDto.getTraineeName()
        );

        List<TrainerTrainingsListResponseDto> responseList = trainings.stream()
                .map(training -> new TrainerTrainingsListResponseDto(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainee().getUser().getFirstName()
                ))
                .toList();

        return ResponseEntity.ok(responseList);
    }
}
