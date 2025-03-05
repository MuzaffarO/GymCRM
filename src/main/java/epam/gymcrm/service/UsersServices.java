package epam.gymcrm.service;

import epam.gymcrm.dto.UserDto;
import epam.gymcrm.model.User;

import java.util.Optional;

public interface UsersServices extends CRUDServices<UserDto, Integer> {

    Optional<User> findByUsername(String username, String password);

    void changePassword(String username, String newPassword, String currentPassword);
}
