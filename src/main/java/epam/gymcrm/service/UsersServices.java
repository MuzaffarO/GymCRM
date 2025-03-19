package epam.gymcrm.service;

import epam.gymcrm.dto.register.TraineeRegisterDto;
import epam.gymcrm.dto.register.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UsersServices {

    Optional<User> findByUsername(String username);

    void changePassword(String username, String newPassword, String currentPassword);

    ResponseEntity<CredentialsInfoResponseDto> registerTrainer(TrainerRegisterDto trainerRegisterDto);

    ResponseEntity<CredentialsInfoResponseDto> registerTrainee(TraineeRegisterDto traineeRegisterDto);

    ResponseEntity<Void> login(String username, String password);
}
