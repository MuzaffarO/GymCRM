package epam.gymcrm.rest;

import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.dto.register.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoDto;
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

    @PostMapping("trainer/register")
    public ResponseEntity<CredentialsInfoDto> registerTrainer(@Valid @RequestBody TrainerRegisterDto trainerRegisterDto) {
        return usersServices.registerTrainer(trainerRegisterDto);
    }


//    @PostMapping("trainee/register")
//    public ResponseEntity<CredentialsInfoDto> registerTrainee(@Valid @RequestBody ) {
//        return usersServices.registerTrainee();
//    }

}
