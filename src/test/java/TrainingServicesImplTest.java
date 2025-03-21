import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dto.request.TrainingRegisterDto;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.impl.TrainingServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServicesImplTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private TrainingServicesImpl trainingServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTraining() {
        TrainingRegisterDto registerDto = new TrainingRegisterDto();
        registerDto.setTraineeUsername("trainee1");
        registerDto.setTrainerUsername("trainer1");
        registerDto.setTrainingName("Yoga");
        registerDto.setTrainingDuration(60.0);

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");

        when(traineeDao.findByUserUsername("trainee1")).thenReturn(java.util.Optional.of(trainee));
        when(trainerDao.findByUserUsername("trainer1")).thenReturn(java.util.Optional.of(trainer));
        when(trainingTypeDao.findByName("Yoga")).thenReturn(java.util.Optional.of(trainingType));

        ResponseEntity<Void> response = trainingServices.createTraining(registerDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void testCreateTraining_TraineeNotFound() {
        TrainingRegisterDto registerDto = new TrainingRegisterDto();
        registerDto.setTraineeUsername("trainee1");

        when(traineeDao.findByUserUsername("trainee1")).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingServices.createTraining(registerDto));
    }

    @Test
    void testCreateTraining_TrainerNotFound() {
        TrainingRegisterDto registerDto = new TrainingRegisterDto();
        registerDto.setTraineeUsername("trainee1");
        registerDto.setTrainerUsername("trainer1");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUserUsername("trainee1")).thenReturn(java.util.Optional.of(trainee));
        when(trainerDao.findByUserUsername("trainer1")).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> trainingServices.createTraining(registerDto));
    }
}