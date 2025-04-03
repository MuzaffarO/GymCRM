package epam.gymcrm.facade;

import epam.gymcrm.dto.trainee.request.TraineeRegisterDto;
import epam.gymcrm.dto.trainer.request.TrainerRegisterDto;
import epam.gymcrm.dto.user.response.CredentialsInfoResponseDto;
import epam.gymcrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public CredentialsInfoResponseDto registerTrainer(TrainerRegisterDto trainerRegisterDto) {
        return userService.registerTrainer(trainerRegisterDto);
    }

    public CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto traineeRegisterDto) {
        return userService.registerTrainee(traineeRegisterDto);
    }

    public void login(String username, String password) {
        userService.login(username, password);
    }

    public void changeLogin(String username, String oldPassword, String newPassword) {
        userService.changeLogin(username, oldPassword, newPassword);
    }
}
