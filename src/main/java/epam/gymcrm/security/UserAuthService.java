package epam.gymcrm.security;//package epam.gymcrm.security;
//
//import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
//import epam.gymcrm.exceptions.UserNotFoundException;
//import epam.gymcrm.model.User;
//import epam.gymcrm.repository.UsersRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class UserAuthService implements AuthServices {
//
//    private final UsersRepository usersRepository;
//
//    @Override
//    public boolean authenticate(String username, String password) {
//        User user = usersRepository.findByUsername(username)
//                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
//
//        if (!user.getPassword().equals(password)) {
//            throw new InvalidUsernameOrPasswordException("Incorrect password!");
//        }
//        return true;
//    }
//
//}

import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthService implements AuthService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final BruteForceProtectionService bruteForceService;

    @Override
    public boolean authenticate(String username, String password) {
        if (bruteForceService.isBlocked(username)) {
            throw new InvalidUsernameOrPasswordException("Account locked for 5 minutes due to multiple failed attempts.");
        }

        User user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            bruteForceService.recordFailedAttempt(username);
            throw new InvalidUsernameOrPasswordException("Incorrect password!");
        }

        bruteForceService.resetAttempts(username); // ✅ reset on success
        return true;
    }
}


