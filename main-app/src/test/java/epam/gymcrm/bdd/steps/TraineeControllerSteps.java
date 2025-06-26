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
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class TraineeControllerSteps extends CommonSteps{

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TraineeRepository traineeRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    private MvcResult result;
    private String jwt;
    private final String BASE_URL = "/trainees";

    @Given("a valid JWT token for {string}")
    public void a_valid_jwt_token(String username) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("TRAINEE")
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    @When("the client requests trainee profile for {string}")
    public void the_client_requests_trainee_profile_for(String username) throws Exception {
        result = mockMvc.perform(get(BASE_URL + "/by-username")
                        .param("username", username)
                        .header("Authorization", jwt))
                .andReturn();
    }

    @When("the client updates trainee profile for {string} with new first name {string}")
    public void update_profile(String username, String firstName) throws Exception {
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest(
                username, firstName, "Last",
                new SimpleDateFormat("dd/MM/yyyy").parse("15/08/2000"),
                "Test Street", true);

        result = mockMvc.perform(put(BASE_URL + "/update-profile")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("the client updates trainee profile with missing required fields")
    public void update_profile_with_missing_fields() throws Exception {
        UpdateTraineeProfileRequest request = new UpdateTraineeProfileRequest();

        result = mockMvc.perform(put(BASE_URL + "/update-profile")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("the client deletes trainee with username {string}")
    public void the_client_deletes_trainee(String username) throws Exception {
        result = mockMvc.perform(delete(BASE_URL + "/delete")
                        .param("username", username)
                        .header("Authorization", jwt))
                .andReturn();
    }

    @When("the client assigns trainers {string} to trainee {string}")
    public void the_client_assigns_trainers_to_trainee(String trainerListStr, String username) throws Exception {
        List<TrainerUsernameRequest> trainerList = Arrays.stream(trainerListStr.split(","))
                .map(String::trim)
                .map(TrainerUsernameRequest::new)
                .toList();

        UpdateTraineeTrainerListRequest request = new UpdateTraineeTrainerListRequest(username, trainerList);

        result = mockMvc.perform(put(BASE_URL + "/update-trainers-list")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("the client changes status of trainee {string} to {string}")
    public void the_client_changes_status(String username, String active) throws Exception {
        boolean isActive = Boolean.parseBoolean(active);
        ActivateDeactivateRequest request = new ActivateDeactivateRequest(username, isActive);

        result = mockMvc.perform(patch(BASE_URL + "/change-status")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }


    @Then("the response should contain trainee first name {string}")
    public void the_response_should_contain_trainee_first_name(String expectedName) throws Exception {
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(expectedName));
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String message) throws Exception {
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(message));
    }

    @Given("a registered trainee with username {string}")
    public void a_registered_trainee_with_username(String username) {
        traineeRepo.findByUserUsername(username).ifPresent(traineeRepo::delete);
        userRepo.findByUsername(username).ifPresent(userRepo::delete);

        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .username(username)
                .password("pass")
                .isActive(true)
                .build();

        Trainee trainee = Trainee.builder()
                .user(user)
                .address("123 Main Street")
                .dateOfBirth(new Date())
                .build();

        traineeRepo.save(trainee);
    }
}
