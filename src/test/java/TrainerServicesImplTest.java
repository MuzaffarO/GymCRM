import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.UpdateTrainerProfileRequestDto;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.service.impl.TrainerServicesImpl;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TrainerServicesImplTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainerServicesImpl trainerServices;

    private Trainer trainer;
    private TrainerProfileResponseDto trainerProfileResponseDto;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        trainer.setUser(new User(1, "John", "Doe", "john.doe", "password", true));
        trainer.setTrainees(List.of());

        trainerProfileResponseDto = new TrainerProfileResponseDto(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                null, // specialization could be null for this example
                trainer.getUser().isActive(),
                List.of() // empty list of trainees for simplicity
        );
    }

    @Test
    void testGetTrainerTrainingsByUsername_Success() {
        String username = "john.doe";

        Training training = new Training();

        TrainingDto trainingDto = new TrainingDto();

        when(trainerDao.getTrainerTrainingsByUsername(username)).thenReturn(List.of(training));

        when(trainingMapper.toDto(training)).thenReturn(trainingDto);

        List<TrainingDto> result = trainerServices.getTrainerTrainingsByUsername(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainingDto, result.get(0));

        verify(trainerDao, times(1)).getTrainerTrainingsByUsername(username);
        verify(trainingMapper, times(1)).toDto(training);
    }

    @Test
    void testGetTrainerTrainingsByUsername_Failure() {
        // Arrange
        String username = "john.doe";
        when(trainerDao.getTrainerTrainingsByUsername(username)).thenThrow(new DatabaseException("Error fetching trainings"));

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            trainerServices.getTrainerTrainingsByUsername(username);
        });
    }

    @Test
    void testGetByUsername_Success() {
        // Arrange
        String username = "john.doe";
        when(trainerDao.findByUserUsername(username)).thenReturn(Optional.of(trainer));

        // Act
        ResponseEntity<TrainerProfileResponseDto> response = trainerServices.getByUsername(username);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(trainerProfileResponseDto, response.getBody());
    }

    @Test
    void testGetByUsername_UserNotFound() {
        // Arrange
        String username = "john.doe";
        when(trainerDao.findByUserUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            trainerServices.getByUsername(username);
        });
        assertEquals("Trainer not found for username: john.doe", exception.getMessage());
    }

    @Test
    void testUpdateProfile_Success() {
        // Arrange
        UpdateTrainerProfileRequestDto requestDto = new UpdateTrainerProfileRequestDto("john.doe", "NewFirstName", "NewLastName", new SpecializationNameDto(""), true);


        UpdateTrainerProfileResponseDto expectedResponse = new UpdateTrainerProfileResponseDto(
                "john.doe", "NewFirstName", "NewLastName", null, true, List.of()
        );

        // Mock the behavior of trainerDao and trainer object
        when(trainerDao.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));

        // Act
        ResponseEntity<UpdateTrainerProfileResponseDto> response = trainerServices.updateProfile(requestDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        // Verify interactions with the mock objects
        verify(trainerDao, times(1)).findByUserUsername("john.doe");
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testGetNotAssignedActiveTrainers_Success() {
        // Arrange
        String username = "john.doe";

        // Create a mock list of Trainers
        Trainer trainer = new Trainer();
        trainer.setUser(new User(2, "trainer.username", "password", "TrainerFirstName", "TrainerLastName", true));
        trainer.setSpecializationType(new TrainingType(1, "Yoga", List.of()));

        // Create a mock TrainerResponseDto list
        TrainerResponseDto trainerResponseDto = new TrainerResponseDto(
                trainer.getUser().getUsername(),
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                new SpecializationNameDto("Yoga")
        );

        // Mock the dao call to return a list of trainers
        when(trainerDao.findNotAssignedActiveTrainers(username)).thenReturn(List.of(trainer));

        // Act
        ResponseEntity<List<TrainerResponseDto>> response = trainerServices.getNotAssignedActiveTrainers(username);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(trainerResponseDto, response.getBody().get(0));
    }


    @Test
    void testChangeStatus_Success() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("john.doe", true);
        when(trainerDao.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));

        // Act
        ResponseEntity<Void> response = trainerServices.changeStatus(requestDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void testChangeStatus_UserNotFound() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("john.doe", true);
        when(trainerDao.findByUserUsername("john.doe")).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            trainerServices.changeStatus(requestDto);
        });
        assertEquals("Trainer not found for username: john.doe", exception.getMessage());
    }

    @Test
    void testChangeStatus_Failure() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("john.doe", true);

        // Mocking the case where the trainer is found
        when(trainerDao.findByUserUsername("john.doe")).thenReturn(Optional.of(trainer));

        // Mocking the case where the update operation fails, simulate a DataAccessException
        doThrow(new DataAccessException("Database error") {
        }).when(trainerDao).update(trainer);

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            trainerServices.changeStatus(requestDto);
        });
        assertEquals("Error while updating trainer status", exception.getMessage());
    }


}
