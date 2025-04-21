package epam.gymcrm.service;

import epam.gymcrm.dto.trainee.request.UpdateTraineeProfileRequest;
import epam.gymcrm.dto.trainee.request.UpdateTraineeTrainerListRequest;
import epam.gymcrm.dto.trainee.response.TraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeProfileResponse;
import epam.gymcrm.dto.trainee.response.UpdateTraineeTrainersResponse;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequest;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.service.impl.TraineeServiceImpl;
import epam.gymcrm.mapper.TraineeMapper;
import epam.gymcrm.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock private TraineeRepository traineeRepository;
    @Mock private TrainerRepository trainerRepository;
    @Mock private TrainingMapper trainingMapper;
    @Mock private TraineeMapper traineeMapper;

    @InjectMocks private TraineeServiceImpl traineeServices;

    private Trainee mockTrainee;

    @BeforeEach
    void setup() {
        User user = new User(1, "John", "Doe", "john.doe", "pass", true);
        mockTrainee = new Trainee(1, new Date(), "Street 1", user, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void testGetByUsername_Success() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));

        TraineeProfileResponse response = traineeServices.getByUsername("john.doe");

        verify(traineeRepository).findByUserUsername("john.doe");
    }

    @Test
    void testGetByUsername_NotFound() {
        when(traineeRepository.findByUserUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> traineeServices.getByUsername("unknown"));
    }

    @Test
    void testUpdateProfile_Success() {
        UpdateTraineeProfileRequest dto = new UpdateTraineeProfileRequest("john.doe", "John", "Doe", new Date(), "New Address", true);
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));
        when(traineeRepository.save(any())).thenReturn(mockTrainee);

        UpdateTraineeProfileResponse response = traineeServices.updateProfile(dto);

        verify(traineeRepository).save(mockTrainee);
    }

    @Test
    void testDeleteByUsername_Success() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));

        traineeServices.deleteByUsername("john.doe");

        verify(traineeRepository).delete(mockTrainee);
    }

    @Test
    void testDeleteByUsername_Failure() {
        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));
        doThrow(mock(DataAccessException.class)).when(traineeRepository).delete(mockTrainee);

        assertThrows(DatabaseException.class, () -> traineeServices.deleteByUsername("john.doe"));
    }

    @Test
    void testChangeStatus_Success() {
        ActivateDeactivateRequest dto = new ActivateDeactivateRequest("john.doe", false);
        mockTrainee.getUser().setActive(true);

        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));
        when(traineeRepository.save(any())).thenReturn(mockTrainee);
        traineeServices.changeStatus(dto);

        assertFalse(mockTrainee.getUser().isActive());
    }

    @Test
    void testUpdateTraineeTrainersList_Success() {
        UpdateTraineeTrainerListRequest dto = new UpdateTraineeTrainerListRequest("john.doe", List.of(new TrainerUsernameRequest("trainer1")));
        Trainer mockTrainer = new Trainer();
        User trainerUser = new User(2, "Trainer", "One", "trainer1", "pass", true);
        mockTrainer.setUser(trainerUser);

        when(traineeRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainee));
        when(trainerRepository.findAllByUsername(List.of("trainer1"))).thenReturn(List.of(mockTrainer));
        when(traineeRepository.save(any())).thenReturn(mockTrainee);

        UpdateTraineeTrainersResponse response = traineeServices.updateTraineeTrainersList(dto);

        assertEquals(1, mockTrainee.getTrainers().size());
    }
}


