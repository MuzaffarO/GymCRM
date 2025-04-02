package epam.gymcrm.service.impl;

import epam.gymcrm.dto.LoginRequest;
import epam.gymcrm.dto.PasswordChangeRequest;
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
import epam.gymcrm.service.UsersService;
import epam.gymcrm.mapper.TrainingTypeMapper;
import epam.gymcrm.security.AuthServices;
import epam.gymcrm.credentials.CredentialGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final TrainingTypeMapper specializationTypeMapper;
    private final UsersRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final CredentialGenerator credentialGenerator;
    private final AuthServices authServices;
    private final TrainingTypeRepository trainingTypeRepository;
    private final MeterRegistry meterRegistry;
    private final PasswordEncoder passwordEncoder;

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
        StringBuilder rawPasswordHolder = new StringBuilder();
        User savedUser = createUser(trainerRegisterDto.getFirstName(), trainerRegisterDto.getLastName(), rawPasswordHolder);

        Trainer trainer = Trainer.builder()
                .specializationType(specialization)
                .user(savedUser)
                .build();

        trainerRepository.save(trainer);

        log.info("New trainer registered: {}", trainer);
        meterRegistry.counter("trainer.registration.count").increment();
        return new CredentialsInfoResponseDto(savedUser.getUsername(), rawPasswordHolder.toString());
    }


    @Override
    public CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto traineeRegisterDto) {
        StringBuilder rawPasswordHolder = new StringBuilder();
        User savedUser = createUser(traineeRegisterDto.getFirstName(), traineeRegisterDto.getLastName(),rawPasswordHolder);

        Trainee trainee = Trainee.builder()
                .user(savedUser)
                .dateOfBirth(traineeRegisterDto.getDateOfBirth())
                .address(traineeRegisterDto.getAddress())
                .build();

        traineeRepository.save(trainee);
        meterRegistry.counter("trainee.registration.count").increment();
        return new CredentialsInfoResponseDto(savedUser.getUsername(), rawPasswordHolder.toString());
    }

    @Override
    public void login(LoginRequest loginRequest) {
        try {
            authServices.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            meterRegistry.counter("gymcrm.login.success").increment();
        }catch (Exception e) {
            meterRegistry.counter("gymcrm.login.failed").increment();
            throw new InvalidUsernameOrPasswordException(e.getMessage());
        }
    }

    @Override
    public void changeLogin(PasswordChangeRequest passwordChangeRequest) {
        authServices.authenticate(passwordChangeRequest.getUsername(), passwordChangeRequest.getOldPassword());
        try {
            String hashedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
            userRepository.changePassword(passwordChangeRequest.getUsername(), hashedNewPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private User createUser(String firstName, String lastName, StringBuilder rawPasswordHolder) {
        String username = credentialGenerator.generateUsername(firstName, lastName);
        String plainPassword = credentialGenerator.generatePassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);

        rawPasswordHolder.append(plainPassword); // store for later use

        return userRepository.save(User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(hashedPassword)
                .isActive(true)
                .build());
    }


}
