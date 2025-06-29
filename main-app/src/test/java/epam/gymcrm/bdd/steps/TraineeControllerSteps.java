package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainee.request.*;
import epam.gymcrm.dto.trainer.request.TrainerUsernameRequest;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class TraineeControllerSteps {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TraineeRepository traineeRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SharedContext sharedContext;
    @Autowired private AuthSteps authSteps;

    private final String BASE_URL = "/trainees";

    @Given("a registered trainee")
    public void a_registered_trainee() {
        String uniqueUsername = "trainee_" + UUID.randomUUID();

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username(uniqueUsername)
                .password("pass")
                .isActive(true)
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address("123 Main Street")
                .dateOfBirth(new Date())
                .build();

        traineeRepo.save(trainee);
        sharedContext.set("username", uniqueUsername);
    }

    @Given("a valid JWT token for that trainee")
    public void jwt_for_registered_trainee() {
        String username = sharedContext.get("username", String.class);

        // Create Spring Security UserDetails (not your JPA User entity)
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("USER")
                .build();

        String token = jwtUtil.generateToken(userDetails);
        authSteps.setJwt("Bearer " + token);
    }

    @When("the client requests trainee profile")
    public void the_client_requests_trainee_profile() throws Exception {
        String username = sharedContext.get("username", String.class);
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL + "/by-username")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client requests trainee profile for {string}")
    public void the_client_requests_trainee_profile_for(String username) throws Exception {
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL + "/by-username")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client updates trainee profile with new first name {string}")
    public void update_profile_with_new_first_name(String firstName) throws Exception {
        String username = sharedContext.get("username", String.class);
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest(
                username, firstName, "Last",
                new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2000"),
                "Test Street", true);

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-profile")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client updates trainee profile with missing required fields")
    public void update_profile_with_missing_fields() throws Exception {
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest();

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-profile")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client deletes the trainee")
    public void delete_the_trainee() throws Exception {
        String username = sharedContext.get("username", String.class);
        sharedContext.setResult(
                mockMvc.perform(delete(BASE_URL + "/delete")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client deletes trainee with username {string}")
    public void the_client_deletes_trainee(String username) throws Exception {
        sharedContext.setResult(
                mockMvc.perform(delete(BASE_URL + "/delete")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client assigns trainers {string} to that trainee")
    public void assign_trainers_to_that_trainee(String trainers) throws Exception {
        String username = sharedContext.get("username", String.class);
        the_client_assigns_trainers_to_trainee(trainers, username);
    }

    @When("the client assigns trainers {string} to trainee {string}")
    public void the_client_assigns_trainers_to_trainee(String trainerListStr, String username) throws Exception {
        List<TrainerUsernameRequest> trainerList = Arrays.stream(trainerListStr.split(","))
                .map(String::trim)
                .map(TrainerUsernameRequest::new)
                .toList();

        UpdateTraineeTrainerListRequest request = new UpdateTraineeTrainerListRequest(username, trainerList);

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-trainers-list")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client changes status of the trainee to {string}")
    public void change_status_of_that_trainee(String active) throws Exception {
        String username = sharedContext.get("username", String.class);
        the_client_changes_status(username, active);
    }

    @When("the client changes status of trainee {string} to {string}")
    public void the_client_changes_status(String username, String active) throws Exception {
        boolean isActive = Boolean.parseBoolean(active);
        ActivateDeactivateRequest request = new ActivateDeactivateRequest(username, isActive);

        sharedContext.setResult(
                mockMvc.perform(patch(BASE_URL + "/change-status")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @Then("the response should contain trainee first name {string}")
    public void the_response_should_contain_trainee_first_name(String expectedName) throws Exception {
        String content = sharedContext.getResult().getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(expectedName));
    }

}
