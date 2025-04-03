package epam.gymcrm.service.impl;

import epam.gymcrm.dto.trainee.request.TraineeRegisterDto;
import epam.gymcrm.dto.trainer.request.TrainerRegisterDto;
import epam.gymcrm.dto.user.response.CredentialsInfoResponseDto;
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
import epam.gymcrm.service.UserService;
import epam.gymcrm.mapper.TrainingTypeMapper;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.credentials.CredentialGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

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
    public CredentialsInfoResponseDto registerTrainer(TrainerRegisterDto trainerRegisterDto) {
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
        return new CredentialsInfoResponseDto(savedUser.getUsername(), savedUser.getPassword());
    }


    @Override
    public CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto traineeRegisterDto) {
        User savedUser = createUser(traineeRegisterDto.getFirstName(), traineeRegisterDto.getLastName());

        Trainee trainee = Trainee.builder()
                .user(savedUser)
                .dateOfBirth(traineeRegisterDto.getDateOfBirth())
                .address(traineeRegisterDto.getAddress())
                .build();

        traineeRepository.save(trainee);
        meterRegistry.counter("trainee.registration.count").increment();
        return new CredentialsInfoResponseDto(savedUser.getUsername(), savedUser.getPassword());
    }

    @Override
    public void login(String username, String password) {
        try {
            authServices.authenticate(username, password);
            meterRegistry.counter("gymcrm.login.success").increment();
        }catch (Exception e) {
            meterRegistry.counter("gymcrm.login.failed").increment();
            throw new InvalidUsernameOrPasswordException(e.getMessage());
        }
    }

    @Override
    public void changeLogin(String username, String oldPassword, String newPassword) {
        authServices.authenticate(username, oldPassword);
        try {
            userRepository.changePassword(username, newPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
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
