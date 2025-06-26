package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.UserRepository;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class TrainingTypeControllerSteps {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SharedContext sharedContext;
    @Autowired private AuthSteps authSteps;
    @Autowired private UserRepository userRepo;

    private static final String BASE_URL = "/training-type";

    @When("the client requests the list of training types")
    public void the_client_requests_training_types() throws Exception {
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL)
                        .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }


    @Given("an admin user exists")
    public void an_admin_user_exists() {
        if (userRepo.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .username("admin")
                    .password("adminpass")
                    .isActive(true)
                    .build();
            userRepo.save(admin);
        }
    }

}
