package epam.gymcrm.controller;

import epam.gymcrm.dto.auth.JwtResponse;
import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegister;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.user.response.CredentialsInfoResponse;
import epam.gymcrm.facade.UserFacade;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/trainer/register")
    public ResponseEntity<CredentialsInfoResponse> registerTrainer(
            @Valid @RequestBody TrainerRegister trainerRegister) {
        return ResponseEntity.ok(userFacade.registerTrainer(trainerRegister));
    }

    @PostMapping("/trainee/register")
    public ResponseEntity<CredentialsInfoResponse> registerTrainee(
            @Valid @RequestBody TraineeRegister traineeRegister) {
        return ResponseEntity.ok(userFacade.registerTrainee(traineeRegister));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userFacade.login(loginRequest));
    }

    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        userFacade.changeLogin(passwordChangeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String message = userFacade.logout(request);
        if (message.equals("No token provided.")) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }
}

