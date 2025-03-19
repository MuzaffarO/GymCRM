package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dao.UserDao;
import epam.gymcrm.dao.datasource.CredentialGenerator;
import epam.gymcrm.dto.register.TraineeRegisterDto;
import epam.gymcrm.dto.register.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.service.UsersServices;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServicesImpl implements UsersServices {

    private final TrainingTypeMapper specializationTypeMapper;
    private final TrainingTypeDao trainingTypeDao;
    private final UserDao userDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final CredentialGenerator credentialGenerator;
    private final AuthServices authServices;

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userDao.findByUsername(username);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void changePassword(String username, String newPassword, String currentPassword) {
        try {
            userDao.changePassword(username, newPassword, currentPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainer(TrainerRegisterDto trainerRegisterDto) {
        TrainingType specialization = trainingTypeDao.findByName(trainerRegisterDto.getSpecialization().getTrainingTypeName())
                .orElseThrow(() -> new DatabaseException("Specialization not found"));

        User savedUser = createUser(trainerRegisterDto.getFirstName(), trainerRegisterDto.getLastName());

        Trainer trainer = Trainer.builder()
                .specializationType(specialization)
                .user(savedUser)
                .build();

        trainerDao.save(trainer).orElseThrow(() -> new DatabaseException("Trainer could not be saved!"));

        log.info("New trainer registered: {}", trainer);

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

        traineeDao.save(trainee).orElseThrow(() -> new DatabaseException("Trainee could not be saved!"));

        return ResponseEntity.ok(new CredentialsInfoResponseDto(savedUser.getUsername(), savedUser.getPassword()));
    }

    @Override
    public ResponseEntity<Void> login(String username, String password) {
        authServices.authenticate(username, password);
        return ResponseEntity.ok().build();
    }

    private User createUser(String firstName, String lastName) {
        String generatedUsername = credentialGenerator.generateUsername(firstName, lastName);
        String generatedPassword = credentialGenerator.generatePassword();

        return userDao.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(generatedUsername)
                .password(generatedPassword)
                .isActive(true)
                .build()
        ).orElseThrow(() -> new DatabaseException("User could not be saved!"));
    }
}
