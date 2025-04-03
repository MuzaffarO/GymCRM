package epam.gymcrm;

import epam.gymcrm.dto.training.TrainingDto;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.user.request.SpecializationNameDto;
import epam.gymcrm.dto.trainer.response.TrainerProfileResponseDto;
import epam.gymcrm.dto.trainer.response.TrainerResponseDto;
import epam.gymcrm.dto.trainer.response.UpdateTrainerProfileResponseDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.service.impl.TrainerServiceImpl;
import epam.gymcrm.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock TrainerRepository trainerRepository;
    @Mock TraineeRepository traineeRepository;
    @Mock TrainingMapper trainingMapper;

    @InjectMocks
    TrainerServiceImpl trainerServices;

    private Trainer mockTrainer;

    @BeforeEach
    void setUp() {
        User user = new User(1, "John", "Doe", "john.doe", "pass", true);
        TrainingType type = new TrainingType(1, "karate", List.of());
        mockTrainer = new Trainer(1, type, user, List.of(), List.of());
    }

    @Test
    void testGetByUsername_Success() {
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainer));

        TrainerProfileResponseDto response = trainerServices.getByUsername("john.doe");

        assertEquals("John", response.firstName());
    }

    @Test
    void testGetByUsername_NotFound() {
        when(trainerRepository.findByUserUsername("missing")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> trainerServices.getByUsername("missing"));
    }

    @Test
    void testUpdateProfile_Success() {
        UpdateTrainerProfileRequestDto dto = new UpdateTrainerProfileRequestDto("john.doe", "John", "Smith", new SpecializationNameDto("karate"), false);
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainer));
        when(trainerRepository.save(any())).thenReturn(mockTrainer);

        UpdateTrainerProfileResponseDto response = trainerServices.updateProfile(dto);

        assertEquals("Smith", response.lastName());
    }

    @Test
    void testGetNotAssignedActiveTrainers_Success() {
        when(traineeRepository.findByUserUsername("trainee1")).thenReturn(Optional.of(mock(Trainee.class)));
        when(trainerRepository.findNotAssignedActiveTrainers("trainee1")).thenReturn(List.of(mockTrainer));

        List<TrainerResponseDto> response = trainerServices.getNotAssignedActiveTrainers("trainee1");

        assertEquals(1, response.size());
        assertEquals("john.doe", response.get(0).username());
    }

    @Test
    void testChangeStatus_Success() {
        ActivateDeactivateRequestDto dto = new ActivateDeactivateRequestDto("john.doe", false);
        when(trainerRepository.findByUserUsername("john.doe")).thenReturn(Optional.of(mockTrainer));

        trainerServices.changeStatus(dto);

        assertFalse(mockTrainer.getUser().isActive());
    }

    @Test
    void testGetTrainerTrainingsByUsername_Success() {
        Training training = new Training(1, null, null, "Session", null, new Date(), 1.5);
        TrainingDto trainingDto = new TrainingDto();

        when(trainerRepository.getTrainerTrainingsByUsername("john.doe")).thenReturn(List.of(training));
        when(trainingMapper.toDto(training)).thenReturn(trainingDto);

        List<TrainingDto> result = trainerServices.getTrainerTrainingsByUsername("john.doe");

        assertEquals(1, result.size());
    }

    @Test
    void testGetTrainerTrainingsByUsername_DbError() {
        when(trainerRepository.getTrainerTrainingsByUsername("john.doe")).thenThrow(mock(DataAccessException.class));
        assertThrows(DatabaseException.class, () -> trainerServices.getTrainerTrainingsByUsername("john.doe"));
    }
}