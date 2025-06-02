package epam.gymcrm.security;

import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthService implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceProtectionService bruteForceService;

    @Override
    public boolean authenticate(String username, String password) {
        if (bruteForceService.isBlocked(username)) {
            throw new InvalidUsernameOrPasswordException("Account locked for 5 minutes due to multiple failed attempts.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            bruteForceService.recordFailedAttempt(username);
            throw new InvalidUsernameOrPasswordException("Incorrect password!");
        }

        bruteForceService.resetAttempts(username);
        return true;
    }
}


