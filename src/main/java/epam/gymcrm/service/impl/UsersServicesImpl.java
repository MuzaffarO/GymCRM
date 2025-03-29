package epam.gymcrm.service.impl;

import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.repository.UsersRepository;
import epam.gymcrm.service.UsersServices;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.credentials.CredentialGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServicesImpl implements UsersServices {

    private final TrainingTypeMapper specializationTypeMapper;
    private final UsersRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final CredentialGenerator credentialGenerator;
    private final AuthServices authServices;
    private final TrainingTypeRepository trainingTypeRepository;
    private final MeterRegistry meterRegistry;

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainer(TrainerRegisterDto trainerRegisterDto) {
        TrainingType specialization = null;
        if (trainerRegisterDto.getSpecialization() != null) {
            specialization = specializationTypeMapper.toEntity(trainerRegisterDto.getSpecialization());
            Optional<TrainingType> existingSpecialization = trainingTypeRepository.findByTrainingTypeName(specialization.getTrainingTypeName());
            if (existingSpecialization.isPresent()) {
                specialization = existingSpecialization.get();
            } else {
                specialization = trainingTypeRepository.save(specialization);
            }
        }

        User savedUser = createUser(trainerRegisterDto.getFirstName(), trainerRegisterDto.getLastName());

        Trainer trainer = Trainer.builder()
                .specializationType(specialization)
                .user(savedUser)
                .build();

        trainerRepository.save(trainer);

        log.info("New trainer registered: {}", trainer);
        meterRegistry.counter("trainer.registration.count").increment();
        return ResponseEntity.ok(new CredentialsInfoResponseDto(savedUser.getUsername(), savedUser.getPassword()));
    }


    @Override
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainee(TraineeRegisterDto traineeRegisterDto) {
        User savedUser = createUser(traineeRegisterDto.getFirstName(), traineeRegisterDto.getLastName());

        Trainee trainee = Trainee.builder()
                .user(savedUser)
                .dateOfBirth(traineeRegisterDto.getDateOfBirth())
                .address(traineeRegisterDto.getAddress())
                .build();

        traineeRepository.save(trainee);
        meterRegistry.counter("trainee.registration.count").increment();
        return ResponseEntity.ok(new CredentialsInfoResponseDto(savedUser.getUsername(), savedUser.getPassword()));
    }

    @Override
    public ResponseEntity<Void> login(String username, String password) {
        try {
            authServices.authenticate(username, password);
            meterRegistry.counter("gymcrm.login.success").increment();
        }catch (Exception e) {
            meterRegistry.counter("gymcrm.login.failed").increment();
            throw new InvalidUsernameOrPasswordException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> changeLogin(String username, String oldPassword, String newPassword) {
        authServices.authenticate(username, oldPassword);
        try {
            userRepository.changePassword(username, newPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    private User createUser(String firstName, String lastName) {
        String generatedUsername = credentialGenerator.generateUsername(firstName, lastName);
        String generatedPassword = credentialGenerator.generatePassword();

        return userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(generatedUsername)
                .password(generatedPassword)
                .isActive(true)
                .build());
    }
}
