package epam.gymcrm;

import epam.gymcrm.dao.TrainerDAO;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainer = new Trainer("Saidikrom", "Yusupov", "saidikrom.yusupov", "trainerPass", true, new TrainingType("Java Programming"));
    }

    @Test
    void shouldRegisterTrainer() {
        trainerService.registerTrainer(trainer);
        verify(trainerDAO, times(1)).save(trainer);
    }

    @Test
    void shouldReturnTrainerWhenFound() {
        when(trainerDAO.findByUsername("saidikrom.yusupov")).thenReturn(Optional.of(trainer));
        Optional<Trainer> foundTrainer = trainerService.selectTrainer("saidikrom.yusupov");
        assertTrue(foundTrainer.isPresent());
        assertEquals("saidikrom.yusupov", foundTrainer.get().getUsername());
    }

    @Test
    void shouldReturnEmptyWhenTrainerNotFound() {
        when(trainerDAO.findByUsername("saidikrom.yusupov")).thenReturn(Optional.empty());
        Optional<Trainer> foundTrainer = trainerService.selectTrainer("saidikrom.yusupov");
        assertFalse(foundTrainer.isPresent());
    }

    @Test
    void shouldReturnAllTrainers() {
        when(trainerDAO.findAll()).thenReturn(List.of(trainer));
        List<Trainer> trainers = trainerService.getAllTrainers();
        assertEquals(1, trainers.size());
    }
}
