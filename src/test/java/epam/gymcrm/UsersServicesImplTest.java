package epam.gymcrm;

import epam.gymcrm.credentials.CredentialGenerator;
import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.model.*;
import epam.gymcrm.repository.*;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.service.impl.UsersServicesImpl;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServicesImplTest {

    @Mock UsersRepository userRepository;
    @Mock TrainerRepository trainerRepository;
    @Mock TraineeRepository traineeRepository;
    @Mock TrainingTypeRepository trainingTypeRepository;
    @Mock CredentialGenerator credentialGenerator;
    @Mock AuthServices authServices;
    @Mock TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    UsersServicesImpl usersServices;

    User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.doe")
                .password("password")
                .isActive(true)
                .build();
    }

    @Test
    void testRegisterTrainer_WithNewSpecialization() {
        TrainerRegisterDto dto = new TrainerRegisterDto("John", "Doe", new TrainingTypeDto(1,"karate"));
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("karate");

        when(trainingTypeMapper.toEntity(dto.getSpecialization())).thenReturn(trainingType);
        when(trainingTypeRepository.findByTrainingTypeName("karate")).thenReturn(Optional.empty());
        when(trainingTypeRepository.save(any())).thenReturn(trainingType);
        when(credentialGenerator.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(credentialGenerator.generatePassword()).thenReturn("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<CredentialsInfoResponseDto> response = usersServices.registerTrainer(dto);

        assertEquals("john.doe", response.getBody().getUsername());
        assertEquals("password", response.getBody().getPassword());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void testRegisterTrainee() {
        TraineeRegisterDto dto = new TraineeRegisterDto("Alice", "Smith", new Date(), "123 Street");
        when(credentialGenerator.generateUsername("Alice", "Smith")).thenReturn("alice.smith");
        when(credentialGenerator.generatePassword()).thenReturn("secure");
        when(userRepository.save(any())).thenReturn(User.builder()
                .firstName("Alice").lastName("Smith").username("alice.smith").password("secure").build());

        ResponseEntity<CredentialsInfoResponseDto> response = usersServices.registerTrainee(dto);

        assertEquals("alice.smith", response.getBody().getUsername());
        assertEquals("secure", response.getBody().getPassword());
        verify(traineeRepository).save(any(Trainee.class));
    }

    @Test
    void testLogin() {
        when(authServices.authenticate("user", "pass")).thenReturn(true);
        ResponseEntity<Void> response = usersServices.login("user", "pass");
        assertEquals(200, response.getStatusCodeValue());
        verify(authServices).authenticate("user", "pass");
    }

    @Test
    void testChangeLogin() {
        when(authServices.authenticate("user", "oldPass")).thenReturn(true);

        ResponseEntity<Void> response = usersServices.changeLogin("user", "oldPass", "newPass");

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository).changePassword("user", "newPass");
    }


    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("john.doe")).thenReturn(Optional.of(user));
        Optional<User> result = usersServices.findByUsername("john.doe");
        assertTrue(result.isPresent());
        assertEquals("john.doe", result.get().getUsername());
    }
}