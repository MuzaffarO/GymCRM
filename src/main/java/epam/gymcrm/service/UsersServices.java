package epam.gymcrm.service;

import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UsersServices {

    Optional<User> findByUsername(String username);

    ResponseEntity<CredentialsInfoResponseDto> registerTrainer(TrainerRegisterDto trainerRegisterDto);

    ResponseEntity<CredentialsInfoResponseDto> registerTrainee(TraineeRegisterDto traineeRegisterDto);

    ResponseEntity<Void> login(String username, String password);

    ResponseEntity<Void> changeLogin(String username, String oldPassword, String newPassword);

}
