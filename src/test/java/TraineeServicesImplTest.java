import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dto.response.*;
import epam.gymcrm.dto.request.*;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.User;
import epam.gymcrm.service.impl.TraineeServicesImpl;
import epam.gymcrm.service.mapper.TrainingMapper;
import epam.gymcrm.service.mapper.TraineeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TraineeServicesImplTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TraineeServicesImpl traineeServices;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setUser(new User(1, "Jane", "Doe", "jane.doe", "password", true));
        trainee.setTrainers(List.of()); // Initialize with an empty list for simplicity
    }

    @Test
    void testGetByUsername_Success() {
        // Arrange
        String username = "jane.doe";
        TraineeProfileResponseDto expectedResponse = new TraineeProfileResponseDto(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth(),
                trainee.getAddress(),
                trainee.getUser().isActive(),
                List.of() // empty list of trainers for simplicity
        );

        when(traineeDao.findByUserUsername(username)).thenReturn(Optional.of(trainee));

        // Act
        ResponseEntity<TraineeProfileResponseDto> response = traineeServices.getByUsername(username);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetByUsername_UserNotFound() {
        // Arrange
        String username = "jane.doe";
        when(traineeDao.findByUserUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            traineeServices.getByUsername(username);
        });
        assertEquals("Trainee not found for username: jane.doe", exception.getMessage());
    }

    @Test
    void testUpdateProfile_Success() {
        // Arrange
        UpdateTraineeProfileRequestDto requestDto = new UpdateTraineeProfileRequestDto(
                "jane.doe", "NewFirstName", "NewLastName", Date.valueOf("2004-03-04"), "NewAddress", true
        );
        Trainee updatedTrainee = new Trainee();
        updatedTrainee.setUser(new User(1, "NewFirstName", "NewLastName", "jane.doe", "password", true));
        updatedTrainee.setAddress("NewAddress");

        UpdateTraineeProfileResponseDto expectedResponse = new UpdateTraineeProfileResponseDto(
                "jane.doe", "NewFirstName", "NewLastName", Date.valueOf("2004-03-04"), "NewAddress", true, List.of());

        when(traineeDao.findByUserUsername("jane.doe")).thenReturn(Optional.of(trainee));
        when(traineeDao.update(trainee)).thenReturn(Optional.of(updatedTrainee));

        // Act
        ResponseEntity<UpdateTraineeProfileResponseDto> response = traineeServices.updateProfile(requestDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());

        verify(traineeDao, times(1)).findByUserUsername("jane.doe");
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void testChangeStatus_Success() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("jane.doe", false);
        when(traineeDao.findByUserUsername("jane.doe")).thenReturn(Optional.of(trainee));

        // Act
        ResponseEntity<Void> response = traineeServices.changeStatus(requestDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void testChangeStatus_UserNotFound() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("jane.doe", true);
        when(traineeDao.findByUserUsername("jane.doe")).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            traineeServices.changeStatus(requestDto);
        });
        assertEquals("Trainee not found for username: jane.doe", exception.getMessage());
    }

    @Test
    void testChangeStatus_Failure() {
        // Arrange
        ActivateDeactivateRequestDto requestDto = new ActivateDeactivateRequestDto("jane.doe", false);

        // Mocking the case where the trainee is found
        when(traineeDao.findByUserUsername("jane.doe")).thenReturn(Optional.of(trainee));

        // Mocking the case where the update operation fails, simulate a DataAccessException
        doThrow(new DataAccessException("Database error") {}).when(traineeDao).update(trainee);

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            traineeServices.changeStatus(requestDto);
        });
        assertEquals("Error while updating trainee status", exception.getMessage());
    }
}
