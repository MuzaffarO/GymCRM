package epam.gymcrm.service;

import epam.gymcrm.dto.trainee.request.TraineeRegisterDto;
import epam.gymcrm.dto.trainer.request.TrainerRegisterDto;
import epam.gymcrm.dto.user.response.CredentialsInfoResponseDto;
import epam.gymcrm.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    CredentialsInfoResponseDto registerTrainer(TrainerRegisterDto trainerRegisterDto);

    CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto traineeRegisterDto);

    void login(String username, String password);

    void changeLogin(String username, String oldPassword, String newPassword);

}
