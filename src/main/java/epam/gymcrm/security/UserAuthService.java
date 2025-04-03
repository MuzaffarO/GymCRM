package epam.gymcrm.security;

import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthService implements AuthServices {

    private final UserRepository userRepository;

    @Override
    public boolean authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        if (!user.getPassword().equals(password)) {
            throw new InvalidUsernameOrPasswordException("Incorrect password!");
        }
        return true;
    }

}
