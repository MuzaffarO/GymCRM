package epam.gymcrm;

import epam.gymcrm.service.GymFacade;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.service.TrainerService;
import epam.gymcrm.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GymFacadeTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTraineeService() {
        assertNotNull(gymFacade.getTraineeService());
    }

    @Test
    void shouldReturnTrainerService() {
        assertNotNull(gymFacade.getTrainerService());
    }

    @Test
    void shouldReturnTrainingService() {
        assertNotNull(gymFacade.getTrainingService());
    }
}
