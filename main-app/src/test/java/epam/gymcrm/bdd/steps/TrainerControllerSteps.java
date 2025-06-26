package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class TrainerControllerSteps extends CommonSteps{

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TrainerRepository trainerRepo;
    @Autowired private UserRepository userRepo;

    private MvcResult result;
    private String jwt;
    private final String BASE_URL = "/trainers";

    @Given("a valid JWT token for {string}")
    public void a_valid_jwt_token(String username) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("dummy")
                .roles("TRAINER")
                .build();

        jwt = "Bearer " + jwtUtil.generateToken(userDetails);
    }

    @Given("a registered trainer with username {string}")
    public void a_registered_trainer_with_username(String username) {
        trainerRepo.findByUserUsername(username).ifPresent(trainerRepo::delete);
        userRepo.findByUsername(username).ifPresent(userRepo::delete);

        User user = User.builder()
                .firstName("Trainer")
                .lastName("One")
                .username(username)
                .password("pass")
                .isActive(true)
                .build();

        TrainingType specialization = TrainingType.builder()
                .trainingTypeName("Fitness")
                .build();

        Trainer trainer = Trainer.builder()
                .user(user)
                .specializationType(specialization)
                .build();

        trainerRepo.save(trainer);
    }

    @When("the client requests trainer profile for {string}")
    public void the_client_requests_trainer_profile(String username) throws Exception {
        result = mockMvc.perform(get(BASE_URL + "/by-username")
                        .param("username", username)
                        .header("Authorization", jwt))
                .andReturn();
    }

    @When("the client updates trainer profile for {string} with new first name {string}")
    public void update_trainer_profile(String username, String newName) throws Exception {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();
        request.setUsername(username);
        request.setFirstName(newName);
        request.setLastName("Updated");
        request.setIsActive(true);

        result = mockMvc.perform(put(BASE_URL + "/update-profile")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("the client updates trainer profile with missing required fields")
    public void update_trainer_profile_with_missing_fields() throws Exception {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();

        result = mockMvc.perform(put(BASE_URL + "/update-profile")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("the client requests unassigned trainers for trainee {string}")
    public void request_unassigned_trainers(String traineeUsername) throws Exception {
        result = mockMvc.perform(get(BASE_URL + "/not-assigned-active")
                        .param("username", traineeUsername)
                        .header("Authorization", jwt))
                .andReturn();
    }

    @When("the client changes trainer status of {string} to {string}")
    public void change_trainer_status(String username, String isActiveStr) throws Exception {
        boolean isActive = Boolean.parseBoolean(isActiveStr);
        ActivateDeactivateRequest request = new ActivateDeactivateRequest(username, isActive);

        result = mockMvc.perform(patch(BASE_URL + "/change-status")
                        .header("Authorization", jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }


    @Then("the response should contain trainer first name {string}")
    public void the_response_should_contain_trainer_first_name(String name) throws Exception {
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(name));
    }
}
