package epam.gymcrm;

import epam.gymcrm.dao.TrainingDAO;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee("Muzaffar", "Obidjonov", "muzaffar.obidjonov", "password123", true, "1995-08-15", "NY, USA");
        trainer = new Trainer("Saidikrom", "Yusupov", "saidikrom.yusupov", "trainerPass", true, new TrainingType("Java Programming"));
        training = new Training(trainee, trainer, "Java Basics", new TrainingType("Java Programming"), null, 5);
    }

    @Test
    void shouldCreateTraining() {
        trainingService.createTraining(training);
        verify(trainingDAO, times(1)).save(training);
    }

    @Test
    void shouldReturnTrainingsWhenFound() {
        when(trainingDAO.findByTrainingName("Java Basics")).thenReturn(List.of(training));
        List<Training> foundTrainings = trainingService.selectTraining("Java Basics");
        assertEquals(1, foundTrainings.size());
        assertEquals("Java Basics", foundTrainings.get(0).getTrainingName());
    }

    @Test
    void shouldReturnEmptyListWhenTrainingNotFound() {
        when(trainingDAO.findByTrainingName("Python Basics")).thenReturn(List.of());
        List<Training> foundTrainings = trainingService.selectTraining("Python Basics");
        assertTrue(foundTrainings.isEmpty());
    }

    @Test
    void shouldReturnAllTrainings() {
        when(trainingDAO.findAll()).thenReturn(List.of(training));
        List<Training> trainings = trainingService.getAllTrainings();
        assertEquals(1, trainings.size());
    }
}
