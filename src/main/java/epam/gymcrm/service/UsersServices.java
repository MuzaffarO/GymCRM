package epam.gymcrm.service;

import epam.gymcrm.dto.register.TrainerRegisterDto;
import epam.gymcrm.dto.response.CredentialsInfoDto;
import epam.gymcrm.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UsersServices {

    Optional<User> findByUsername(String username, String password);

    void changePassword(String username, String newPassword, String currentPassword);

    ResponseEntity<CredentialsInfoDto> registerTrainer(TrainerRegisterDto trainerRegisterDto);
}
