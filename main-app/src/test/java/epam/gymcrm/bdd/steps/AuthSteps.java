package epam.gymcrm.bdd.steps;

import epam.gymcrm.model.User;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.Given;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class AuthSteps {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private SharedContext sharedContext;
    @Autowired private PasswordEncoder passwordEncoder;

    @Setter
    private String jwt;

//    @Given("a valid JWT token for {string}")
//    public void a_valid_jwt_token(String username) {
//        UserDetails userDetails = org.springframework.security.core.userdetails.User
//                .withUsername(username)
//                .password("dummy")
//                .roles("USER") // or "ADMIN"
//                .build();
//
//        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
//    }

    @Given("a valid JWT token for {string}")
    public void a_valid_jwt_token(String username) {
        createTestUser(username, "dummy"); // ensure user is saved to DB
        generateTokenFor(username);
    }


    public void generateTokenFor(String username) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("USER")
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    public void createTestUser(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .firstName("Test")
                    .lastName("User")
                    .isActive(true)
                    .build();
            userRepository.save(user);
            System.out.println("Encoded password stored: " + user.getPassword());
        }

        sharedContext.set("username", username);
    }

}
