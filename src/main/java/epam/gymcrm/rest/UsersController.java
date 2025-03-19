package epam.gymcrm.rest;

import epam.gymcrm.dto.request.TraineeRegisterDto;
import epam.gymcrm.dto.request.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoResponseDto;
import epam.gymcrm.service.UsersServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersServices usersServices;

    @PostMapping("/trainer/register")
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainer(@Valid @RequestBody TrainerRegisterDto trainerRegisterDto) {
        return usersServices.registerTrainer(trainerRegisterDto);
    }

    @PostMapping("/trainee/register")
    public ResponseEntity<CredentialsInfoResponseDto> registerTrainee(@Valid @RequestBody TraineeRegisterDto traineeRegisterDto) {
        return usersServices.registerTrainee(traineeRegisterDto);
    }

    @GetMapping("/login")
    public ResponseEntity<Void> login(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password) {
        return usersServices.login(username, password);
    }

    @PutMapping("/change-login")
    public ResponseEntity<Void> changeLogin(@RequestParam(name = "username") String username,
                                            @RequestParam(name = "oldPassword") String oldPassword,
                                            @RequestParam(name = "newPassword") String newPassword) {
        return usersServices.changeLogin(username, oldPassword, newPassword);
    }


}
