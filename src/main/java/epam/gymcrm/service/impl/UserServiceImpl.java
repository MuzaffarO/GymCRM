package epam.gymcrm.service.impl;

import epam.gymcrm.dto.auth.JwtResponse;
import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegister;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import epam.gymcrm.security.TokenBlacklistService;
import epam.gymcrm.service.UserService;
import epam.gymcrm.mapper.TrainingTypeMapper;
import epam.gymcrm.security.AuthService;
import epam.gymcrm.credentials.CredentialGenerator;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final TrainingTypeMapper specializationTypeMapper;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final CredentialGenerator credentialGenerator;
    private final AuthService authService;
    private final TrainingTypeRepository trainingTypeRepository;
    private final MeterRegistry meterRegistry;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;


    @Override
    public Optional<User> findByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public CredentialsInfoResponse registerTrainer(TrainerRegister trainerRegister) {
        TrainingType specialization = null;
        if (trainerRegister.getSpecialization() != null) {
            specialization = specializationTypeMapper.toEntity(trainerRegister.getSpecialization());
            Optional<TrainingType> existingSpecialization = trainingTypeRepository.findByTrainingTypeName(specialization.getTrainingTypeName());
            if (existingSpecialization.isPresent()) {
                specialization = existingSpecialization.get();
            } else {
                specialization = trainingTypeRepository.save(specialization);
            }
        }
        StringBuilder rawPasswordHolder = new StringBuilder();
        User savedUser = createUser(trainerRegister.getFirstName(), trainerRegister.getLastName(), rawPasswordHolder);

        Trainer trainer = Trainer.builder()
                .specializationType(specialization)
                .user(savedUser)
                .build();

        trainerRepository.save(trainer);

        log.info("New trainer registered: {}", trainer);
        meterRegistry.counter("trainer.registration.count").increment();
        return new CredentialsInfoResponse(savedUser.getUsername(), rawPasswordHolder.toString());
    }


    @Override
    public CredentialsInfoResponse registerTrainee(TraineeRegister traineeRegister) {
        StringBuilder rawPasswordHolder = new StringBuilder();
        User savedUser = createUser(traineeRegister.getFirstName(), traineeRegister.getLastName(),rawPasswordHolder);

        Trainee trainee = Trainee.builder()
                .user(savedUser)
                .dateOfBirth(traineeRegister.getDateOfBirth())
                .address(traineeRegister.getAddress())
                .build();

        traineeRepository.save(trainee);
        meterRegistry.counter("trainee.registration.count").increment();
        return new CredentialsInfoResponse(savedUser.getUsername(), rawPasswordHolder.toString());
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        try {
            authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String jwt = jwtUtil.generateToken(userDetails);
            meterRegistry.counter("gymcrm.login.success").increment();
            return new JwtResponse(jwt);
        }catch (Exception e) {
            meterRegistry.counter("gymcrm.login.failed").increment();
            throw new InvalidUsernameOrPasswordException(e.getMessage());
        }
    }

    @Override
    public void changeLogin(PasswordChangeRequest passwordChangeRequest) {
        authService.authenticate(passwordChangeRequest.getUsername(), passwordChangeRequest.getOldPassword());
        try {
            String hashedNewPassword = passwordEncoder.encode(passwordChangeRequest.getNewPassword());
            userRepository.changePassword(passwordChangeRequest.getUsername(), hashedNewPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No token provided.";
        }
        String token = authHeader.substring(7);
        long expiryMillis = jwtUtil.getRemainingExpirationMillis(token);
        tokenBlacklistService.blacklistToken(token, expiryMillis);
        return "Logged out and token blacklisted.";
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
