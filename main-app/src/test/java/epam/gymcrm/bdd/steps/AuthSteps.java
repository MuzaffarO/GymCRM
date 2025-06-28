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

    @Given("a valid JWT token for {string}")
    public void a_valid_jwt_token(String username) {
        createTestUser(username, "dummy"); // ensure user is saved to DB
        generateTokenFor(username);
    }


    public void generateTokenFor(String username) {
        User user = userRepository.findByUsername(username.toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    public void createTestUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> User.builder().username(username).build());

        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName("Test");
        user.setLastName("User");
        user.setActive(true);

        userRepository.save(user);
        sharedContext.set("username", username);
    }


}
