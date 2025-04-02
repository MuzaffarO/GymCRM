package epam.gymcrm.service;

import epam.gymcrm.dto.LoginRequest;
import epam.gymcrm.dto.PasswordChangeRequest;
import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.model.User;

import java.util.Optional;

public interface UsersService {

    Optional<User> findByUsername(String username);

    CredentialsInfoResponseDto registerTrainer(TrainerRegisterDto trainerRegisterDto);

    CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto traineeRegisterDto);

    void login(LoginRequest loginRequest);

    void changeLogin(PasswordChangeRequest passwordChangeRequest);

}
