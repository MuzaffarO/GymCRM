package epam.gymcrm.service;

import epam.gymcrm.dto.trainee.request.TraineeRegisterRequest;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    CredentialsInfoResponse registerTrainer(TrainerRegister trainerRegister);

    CredentialsInfoResponse registerTrainee(TraineeRegisterRequest traineeRegisterRequest);

    void login(String username, String password);

    void changeLogin(String username, String oldPassword, String newPassword);

}
