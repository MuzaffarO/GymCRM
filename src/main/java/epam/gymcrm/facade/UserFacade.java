package epam.gymcrm.facade;

import epam.gymcrm.dto.auth.JwtResponse;
import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegister;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.security.JwtUtil;
import epam.gymcrm.security.TokenBlacklistService;
import epam.gymcrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public CredentialsInfoResponse registerTrainer(TrainerRegister dto) {
        return userService.registerTrainer(dto);
    }

    public CredentialsInfoResponse registerTrainee(TraineeRegister dto) {
        return userService.registerTrainee(dto);
    }

    public JwtResponse login(LoginRequest request) {return userService.login(request);}

    public void changeLogin(PasswordChangeRequest request) {userService.changeLogin(request);}

    public String logout(HttpServletRequest request) {return userService.logout(request);}
}
