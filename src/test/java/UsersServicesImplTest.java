import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dao.UserDao;
import epam.gymcrm.dao.datasource.CredentialGenerator;
import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.dto.request.ActivateDeactivateRequestDto;
import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.dto.response.SpecializationNameDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.service.impl.UsersServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UsersServicesImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private CredentialGenerator credentialGenerator;

    @Mock
    private AuthServices authServices;

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @InjectMocks
    private UsersServicesImpl usersServices;

    private User user;
    private Trainer trainer;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        user = new User(1, "John", "Doe", "john.doe", "password", true);
        trainer = new Trainer();
        trainer.setUser(user);
        trainee = new Trainee();
        trainee.setUser(user);
    }

    @Test
    void testFindByUsername_Success() {
        // Arrange
        when(userDao.findByUsername("john.doe")).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = usersServices.findByUsername("john.doe");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(user, foundUser.get());

        verify(userDao, times(1)).findByUsername("john.doe");
    }

    @Test
    void testFindByUsername_UserNotFound() {
        // Arrange
        when(userDao.findByUsername("john.doe")).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = usersServices.findByUsername("john.doe");

        // Assert
        assertFalse(foundUser.isPresent());

        verify(userDao, times(1)).findByUsername("john.doe");
    }

    @Test
    void testRegisterTrainer_Success() {
        // Arrange
        TrainerRegisterDto registerDto = new TrainerRegisterDto("John", "Doe", new TrainingTypeDto(1,"Yoga"));
        when(trainingTypeDao.findByName("Yoga")).thenReturn(Optional.of(new TrainingType()));
        when(userDao.save(any(User.class))).thenReturn(Optional.of(user));
        when(trainerDao.save(any(Trainer.class))).thenReturn(Optional.of(trainer));

        // Act
        ResponseEntity<CredentialsInfoResponseDto> response = usersServices.registerTrainer(registerDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        assertEquals(user.getPassword(), response.getBody().getPassword());

        verify(trainingTypeDao, times(1)).findByName("Yoga");
        verify(userDao, times(1)).save(any(User.class));
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void testRegisterTrainer_Failure_SpecializationNotFound() {
        // Arrange
        TrainerRegisterDto registerDto = new TrainerRegisterDto("John", "Doe", new TrainingTypeDto(1,"Yoga"));
        when(trainingTypeDao.findByName("Yoga")).thenReturn(Optional.empty());

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            usersServices.registerTrainer(registerDto);
        });
        assertEquals("Specialization not found", exception.getMessage());

        verify(trainingTypeDao, times(1)).findByName("Yoga");
        verify(userDao, never()).save(any(User.class));
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    @Test
    void testRegisterTrainee_Success() {
        // Arrange
        TraineeRegisterDto registerDto = new TraineeRegisterDto("Jane", "Doe", Date.valueOf("1990-01-01"), "Some Address");
        when(userDao.save(any(User.class))).thenReturn(Optional.of(user));
        when(traineeDao.save(any(Trainee.class))).thenReturn(Optional.of(trainee));

        // Act
        ResponseEntity<CredentialsInfoResponseDto> response = usersServices.registerTrainee(registerDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        assertEquals(user.getPassword(), response.getBody().getPassword());

        verify(userDao, times(1)).save(any(User.class));
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void testLogin_Success() {
        // Arrange
        when(authServices.authenticate("john.doe", "password")).thenReturn(true);

        // Act
        ResponseEntity<Void> response = usersServices.login("john.doe", "password");

        // Assert
        assertEquals(200, response.getStatusCodeValue());

        verify(authServices, times(1)).authenticate("john.doe", "password");
    }

    @Test
    void testChangeLogin_Success() {
        // Arrange
        when(authServices.authenticate("john.doe", "password")).thenReturn(true);
        when(userDao.changePassword("john.doe", "newPassword")).thenReturn(true);

        // Act
        ResponseEntity<Void> response = usersServices.changeLogin("john.doe", "password", "newPassword");

        // Assert
        assertEquals(200, response.getStatusCodeValue());

        verify(authServices, times(1)).authenticate("john.doe", "password");
        verify(userDao, times(1)).changePassword("john.doe", "newPassword");
    }

    @Test
    void testChangeLogin_Failure() {
        // Arrange
        when(authServices.authenticate("john.doe", "password")).thenThrow(new DatabaseException("Authentication failed"));

        // Act & Assert
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            usersServices.changeLogin("john.doe", "password", "newPassword");
        });
        assertEquals("Authentication failed", exception.getMessage());

        verify(authServices, times(1)).authenticate("john.doe", "password");
        verify(userDao, never()).changePassword("john.doe", "newPassword");
    }
}
