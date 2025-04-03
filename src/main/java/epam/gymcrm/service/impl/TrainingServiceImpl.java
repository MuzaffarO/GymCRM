package epam.gymcrm.service.impl;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.TrainingTypeNotMatchingException;
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
import epam.gymcrm.service.TrainingService;
import epam.gymcrm.mapper.TrainingMapper;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final MeterRegistry meterRegistry;

    public TrainingServiceImpl(TrainingRepository trainingRepository, TrainingMapper trainingMapper, TrainerRepository trainerRepository, TraineeRepository traineeRepository, TrainingTypeRepository trainingTypeRepository, MeterRegistry meterRegistry) {
        this.trainingRepository = trainingRepository;
        this.trainingMapper = trainingMapper;
        this.trainerRepository = trainerRepository;
        this.traineeRepository = traineeRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void createTraining(TrainingRegister trainingRegister) {
        Trainee trainee = traineeRepository.findByUserUsername(trainingRegister.getTraineeUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainee not found with username: " + trainingRegister.getTraineeUsername()));

        Trainer trainer = trainerRepository.findByUserUsername(trainingRegister.getTrainerUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainer not found with username: " + trainingRegister.getTrainerUsername()));

        TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainingRegister.getTrainingName())
                .orElseThrow(() -> new DatabaseException("Training type not found: " + trainingRegister.getTrainingName()));

        if(!trainer.getSpecializationType().equals(trainingType) ) {
            throw new TrainingTypeNotMatchingException("Training type does not match trainer's specialization");
        }

        if (!trainee.getTrainers().contains(trainer)) {
            trainee.getTrainers().add(trainer);
        } // adding to the entity trainee_trainer as well

        if (!trainer.getTrainees().contains(trainee)) {
            trainer.getTrainees().add(trainee);
        } // adding to the trainer's trainee list

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        training.setTrainingName(trainingRegister.getTrainingName());
        training.setTrainingDate(trainingRegister.getTrainingDate());
        training.setTrainingDuration(trainingRegister.getTrainingDuration());

        trainingRepository.save(training);
        meterRegistry.counter("gymcrm.training.created").increment();
    }

    @Override
    @Timed(value = "gymcrm.trainee.trainings.get", description = "Time to fetch trainee trainings")
    public List<TraineeTrainingsListResponse> getTraineeTrainings(TraineeTrainingsRequest trainingsRequestDto) {
        List<Training> trainings = trainingRepository.findAll(
                TrainingSpecification.findTraineeTrainingsByFilters(
                        trainingsRequestDto.getUsername(),
                        trainingsRequestDto.getPeriodFrom(),
                        trainingsRequestDto.getPeriodTo(),
                        trainingsRequestDto.getTrainerName(),
                        trainingsRequestDto.getTrainingType()
                )
        );

        return trainings.stream()
                .map(training -> new TraineeTrainingsListResponse(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName()
                ))
                .toList();
    }

    @Override
    @Timed(value = "gymcrm.trainer.trainings.get", description = "Time to fetch trainer trainings")
    public List<TrainerTrainingsListResponse> getTrainerTrainings(TrainerTrainingsRequest trainerTrainingsRequest) {

        Trainer trainer = trainerRepository.findByUserUsername(trainerTrainingsRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Trainer not found with username: " + trainerTrainingsRequest.getUsername()));

        List<Training> trainings = trainingRepository.findAll(
                TrainerSpecification.findTrainerTrainingsByFilters(
                        trainerTrainingsRequest.getUsername(),
                        trainerTrainingsRequest.getPeriodFrom(),
                        trainerTrainingsRequest.getPeriodTo(),
                        trainerTrainingsRequest.getTraineeName()
                )
        );

        List<TrainerTrainingsListResponse> responseList = trainings.stream()
                .map(training -> new TrainerTrainingsListResponse(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainee().getUser().getFirstName()
                ))
                .toList();

        return responseList;
    }
}
