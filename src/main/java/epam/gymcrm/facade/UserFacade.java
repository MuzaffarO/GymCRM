package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeRegisterRequest;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public CredentialsInfoResponse registerTrainer(TrainerRegister trainerRegister) {
        return userService.registerTrainer(trainerRegister);
    }

    public CredentialsInfoResponse registerTrainee(TraineeRegisterRequest traineeRegisterRequest) {
        return userService.registerTrainee(traineeRegisterRequest);
    }

    public void login(String username, String password) {
        userService.login(username, password);
    }

    public void changeLogin(String username, String oldPassword, String newPassword) {
        userService.changeLogin(username, oldPassword, newPassword);
    }
}
