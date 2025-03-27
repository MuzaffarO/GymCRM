package epam.gymcrm.service.impl;

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
import epam.gymcrm.repository.TrainingRepository;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.repository.specifications.TrainerSpecification;
import epam.gymcrm.repository.specifications.TrainingSpecification;
import epam.gymcrm.service.TrainingServices;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServicesImpl implements TrainingServices {

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    public TrainingServicesImpl(TrainingRepository trainingRepository, TrainingMapper trainingMapper, TrainerRepository trainerRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Override
    public ResponseEntity<Void> createTraining(TrainingRegisterDto trainingRegisterDto) {
        Trainee trainee = traineeRepository.findByUserUsername(trainingRegisterDto.getTraineeUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainee not found with username: " + trainingRegisterDto.getTraineeUsername()));

        Trainer trainer = trainerRepository.findByUserUsername(trainingRegisterDto.getTrainerUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainer not found with username: " + trainingRegisterDto.getTrainerUsername()));

        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainingRegisterDto.getTrainingName())
                .orElseThrow(() -> new DatabaseException("Training type not found: " + trainingRegisterDto.getTrainingName()));

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingName(trainingRegisterDto.getTrainingName());
        training.setTrainingDate(trainingRegisterDto.getTrainingDate());
        training.setTrainingDuration(trainingRegisterDto.getTrainingDuration());

        trainingRepository.save(training);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<TraineeTrainingsListResponseDto>> getTraineeTrainings(TraineeTrainingsRequestDto trainingsRequestDto) {
        List<Training> trainings = trainingRepository.findAll(
                TrainingSpecification.findTraineeTrainingsByFilters(
                        trainingsRequestDto.getUsername(),
                        trainingsRequestDto.getPeriodFrom(),
                        trainingsRequestDto.getPeriodTo(),
                        trainingsRequestDto.getTrainerName(),
                        trainingsRequestDto.getTrainingType()
                )
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
        List<Training> trainings = trainingRepository.findAll(
                TrainerSpecification.findTrainerTrainingsByFilters(
                        trainerTrainingsRequestDto.getUsername(),
                        trainerTrainingsRequestDto.getPeriodFrom(),
                        trainerTrainingsRequestDto.getPeriodTo(),
                        trainerTrainingsRequestDto.getTraineeName()
                )
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
