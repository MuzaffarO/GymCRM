package epam.gymcrm.bdd.steps;

import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.Given;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class AuthSteps {

    @Autowired private JwtUtil jwtUtil;

    private String jwt;

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Given("a valid JWT token for {string}")
    public void a_valid_jwt_token(String username) {
        UserDetails userDetails = User
                .withUsername(username)
                .password("dummy")
                .roles("USER") // Adjust role as needed
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    public void generateTokenFor(String username) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("USER")
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

}
