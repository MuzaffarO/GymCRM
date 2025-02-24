package epam.gymcrm;

import epam.gymcrm.dao.TraineeDAO;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee("Muzaffar", "Obidjonov", "muzaffar.obidjonov", "password123", true, "1995-08-15", "NY, USA");
    }

    @Test
    void shouldRegisterTrainee() {
        traineeService.registerTrainee(trainee);
        verify(traineeDAO, times(1)).save(trainee);
    }

    @Test
    void shouldReturnTraineeWhenFound() {
        when(traineeDAO.findByUsername("muzaffar.obidjonov")).thenReturn(Optional.of(trainee));
        Optional<Trainee> foundTrainee = traineeService.selectTrainee("muzaffar.obidjonov");
        assertTrue(foundTrainee.isPresent());
        assertEquals("muzaffar.obidjonov", foundTrainee.get().getUsername());
    }

    @Test
    void shouldReturnEmptyWhenTraineeNotFound() {
        when(traineeDAO.findByUsername("muzaffar.obidjonov")).thenReturn(Optional.empty());
        Optional<Trainee> foundTrainee = traineeService.selectTrainee("muzaffar.obidjonov");
        assertFalse(foundTrainee.isPresent());
    }

    @Test
    void shouldReturnAllTrainees() {
        when(traineeDAO.findAll()).thenReturn(List.of(trainee));
        List<Trainee> trainees = traineeService.getAllTrainees();
        assertEquals(1, trainees.size());
    }

    @Test
    void shouldDeleteTrainee() {
        when(traineeDAO.delete("muzaffar.obidjonov")).thenReturn(true);
        traineeService.deleteTrainee("muzaffar.obidjonov");
        verify(traineeDAO, times(1)).delete("muzaffar.obidjonov");
    }
}
