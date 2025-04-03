package epam.gymcrm;

import epam.gymcrm.dto.trainee.request.TraineeTrainingsRequest;
import epam.gymcrm.dto.trainer.request.TrainerTrainingsRequest;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.dto.trainee.response.TraineeTrainingsListResponse;
import epam.gymcrm.dto.trainer.response.TrainerTrainingsListResponse;
import epam.gymcrm.exceptions.TrainingTypeNotMatchingException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.*;
import epam.gymcrm.repository.*;
import epam.gymcrm.service.impl.TrainingServiceImpl;
import epam.gymcrm.mapper.TrainingMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import io.micrometer.core.instrument.Counter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @Mock private TrainingRepository trainingRepository;
    @Mock private TrainingMapper trainingMapper;
    @Mock private TrainerRepository trainerRepository;
    @Mock private TraineeRepository traineeRepository;
    @Mock private TrainingTypeRepository trainingTypeRepository;
    @Mock private MeterRegistry meterRegistry;
    @Mock private Counter mockCounter;


    @InjectMocks private TrainingServiceImpl trainingServices;

    private Trainee trainee;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setup() {
        User traineeUser = new User(1, "Alice", "Wonder", "alice.wonder", "pass", true);
        User trainerUser = new User(2, "Bob", "Trainer", "bob.trainer", "pass", true);
        trainee = new Trainee(1, new Date(), "Street", traineeUser, new ArrayList<>(), new ArrayList<>());
        trainer = new Trainer(1, null, trainerUser, new ArrayList<>(), new ArrayList<>());
        trainingType = new TrainingType(1, "karate", new ArrayList<>());
        trainer.setSpecializationType(trainingType);
    }

    @Test
    void testCreateTraining_Success() {
        TrainingRegister dto = new TrainingRegister("alice.wonder", "bob.trainer", "karate", new Date(), 1.5);
        when(meterRegistry.counter(anyString())).thenReturn(mockCounter);
        when(traineeRepository.findByUserUsername("alice.wonder")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("bob.trainer")).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingTypeName("karate")).thenReturn(Optional.of(trainingType));

        trainingServices.createTraining(dto);

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TrainerSpecializationMismatch() {
        TrainingRegister dto = new TrainingRegister("alice.wonder", "bob.trainer", "boxing", new Date(), 1.5);

        when(traineeRepository.findByUserUsername("alice.wonder")).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername("bob.trainer")).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByTrainingTypeName("boxing")).thenReturn(Optional.of(new TrainingType(2, "boxing", new ArrayList<>())));

        assertThrows(TrainingTypeNotMatchingException.class, () -> trainingServices.createTraining(dto));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        TrainingRegister dto = new TrainingRegister("unknown", "bob.trainer", "karate", new Date(), 1.5);

        when(traineeRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingServices.createTraining(dto));
    }

    @Test
    void testGetTraineeTrainings() {
        TraineeTrainingsRequest dto = new TraineeTrainingsRequest("alice.wonder", null, null, null, null);
        Training training = new Training(1, trainee, trainer, "karate", trainingType, new Date(), 1.0);

        when(trainingRepository.findAll(any(Specification.class))).thenReturn(List.of(training));

        List<TraineeTrainingsListResponse> response = trainingServices.getTraineeTrainings(dto);

        assertEquals(1, response.size());
        assertEquals("karate", response.get(0).trainingType());
    }

    @Test
    void testGetTrainerTrainings() {
        TrainerTrainingsRequest dto = new TrainerTrainingsRequest("bob.trainer", null, null, null);
        Training training = new Training(1, trainee, trainer, "karate", trainingType, new Date(), 1.0);

        when(trainerRepository.findByUserUsername("bob.trainer")).thenReturn(Optional.of(trainer));
        when(trainingRepository.findAll(any(Specification.class))).thenReturn(List.of(training));

        List<TrainerTrainingsListResponse> response = trainingServices.getTrainerTrainings(dto);

        assertEquals(1, response.size());
        assertEquals("karate", response.get(0).trainingType());
    }

    @Test
    void testGetTrainerTrainings_TrainerNotFound() {
        TrainerTrainingsRequest dto = new TrainerTrainingsRequest("unknown", null, null, null);
        when(trainerRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingServices.getTrainerTrainings(dto));
    }
}