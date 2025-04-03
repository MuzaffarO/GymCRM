package epam.gymcrm.service;

import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegister;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    CredentialsInfoResponse registerTrainer(TrainerRegister trainerRegister);

    CredentialsInfoResponse registerTrainee(TraineeRegister traineeRegister);

    void login(LoginRequest loginRequest);

    void changeLogin(PasswordChangeRequest passwordChangeRequest);

}
