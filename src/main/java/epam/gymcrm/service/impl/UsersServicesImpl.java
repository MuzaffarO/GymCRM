package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dao.UserDao;
import epam.gymcrm.dao.datasource.CredentialGenerator;
import epam.gymcrm.dto.register.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.service.UsersServices;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import epam.gymcrm.service.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final TrainingTypeDao trainingTypeDao;
    private final UserDao userDao;
    private final TrainerDao trainerDao;
    private final CredentialGenerator credentialGenerator;

    @Override
    public Optional<User> findByUsername(String username, String password) {
        try {
            return userDao.findByUsername(username, password);
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
    public ResponseEntity<CredentialsInfoDto> registerTrainer(TrainerRegisterDto trainerRegisterDto) {

        TrainingType specialization = trainingTypeDao.findByName(trainerRegisterDto.getSpecialization().getTrainingTypeName())
                .orElseThrow(() -> new DatabaseException("Specialization not found"));

        String generatedUsername = credentialGenerator.generateUsername(trainerRegisterDto.getFirstName(), trainerRegisterDto.getLastName());
        String generatedPassword = credentialGenerator.generatePassword();

        User savedUser = userDao.save(User.builder()
                .firstName(trainerRegisterDto.getFirstName())
                .lastName(trainerRegisterDto.getLastName())
                .username(generatedUsername)
                .password(generatedPassword)
                .isActive(true)
                .build()
        ).orElseThrow(() -> new DatabaseException("User not found"));

        Trainer trainer = trainerDao.save(
            Trainer.builder()
                    .specializationType(specialization)
                    .user(savedUser)
                    .build()).orElseThrow(() -> new DatabaseException("Trainer could not be saved!"));
        log.info("New trainer registered: {}", trainer.toString());

        return ResponseEntity.ok(new CredentialsInfoDto(generatedUsername, generatedPassword));
    }
}
