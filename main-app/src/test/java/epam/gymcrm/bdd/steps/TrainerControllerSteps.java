package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainer.request.UpdateTrainerProfileRequest;
import epam.gymcrm.dto.user.request.ActivateDeactivateRequest;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.model.User;
import epam.gymcrm.repository.TrainerRepository;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class TrainerControllerSteps {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private TrainerRepository trainerRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private SharedContext sharedContext;
    @Autowired private AuthSteps authSteps;
    @Autowired private TrainingTypeRepository trainingTypeRepo;

    private final String BASE_URL = "/trainers";

    @Given("a registered trainer")
    public void a_registered_trainer() {
        String uniqueUsername = "trainer_" + UUID.randomUUID();

        TrainingType specialization = trainingTypeRepo.findByTrainingTypeName("Fitness")
                .orElseGet(() -> trainingTypeRepo.save(
                        TrainingType.builder().trainingTypeName("Fitness").build()));

        User user = User.builder()
                .firstName("Trainer")
                .lastName("One")
                .username(uniqueUsername)
                .password("pass")
                .isActive(true)
                .build();

        Trainer trainer = Trainer.builder()
                .user(user)
                .specializationType(specialization)
                .build();

        trainerRepo.save(trainer);
        sharedContext.set("trainerUsername", uniqueUsername);
    }

    @Given("a valid JWT token for that trainer")
    public void jwt_for_registered_trainer() {
        String username = sharedContext.get("trainerUsername", String.class);
        authSteps.generateTokenFor(username);
    }

    @When("the client requests trainer profile")
    public void the_client_requests_trainer_profile() throws Exception {
        String username = sharedContext.get("trainerUsername", String.class);
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL + "/by-username")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client requests trainer profile for {string}")
    public void the_client_requests_trainer_profile_for(String username) throws Exception {
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL + "/by-username")
                                .param("username", username)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client updates trainer profile with new first name {string}")
    public void update_trainer_profile(String newName) throws Exception {
        String username = sharedContext.get("trainerUsername", String.class);
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();
        request.setUsername(username);
        request.setFirstName(newName);
        request.setLastName("Updated");
        request.setIsActive(true);

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-profile")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client updates trainer profile with missing required fields")
    public void update_trainer_profile_with_missing_fields() throws Exception {
        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-profile")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client requests unassigned trainers for trainee {string}")
    public void request_unassigned_trainers(String traineeUsername) throws Exception {
        sharedContext.setResult(
                mockMvc.perform(get(BASE_URL + "/not-assigned-active")
                                .param("username", traineeUsername)
                                .header("Authorization", authSteps.getJwt()))
                        .andReturn()
        );
    }

    @When("the client changes trainer status to {string}")
    public void change_trainer_status(String isActiveStr) throws Exception {
        boolean isActive = Boolean.parseBoolean(isActiveStr);
        String username = sharedContext.get("trainerUsername", String.class);
        ActivateDeactivateRequest request = new ActivateDeactivateRequest(username, isActive);

        sharedContext.setResult(
                mockMvc.perform(patch(BASE_URL + "/change-status")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @When("the client changes trainer status of {string} to {string}")
    public void change_trainer_status_of(String username, String isActiveStr) throws Exception {
        boolean isActive = Boolean.parseBoolean(isActiveStr);
        ActivateDeactivateRequest request = new ActivateDeactivateRequest(username, isActive);

        sharedContext.setResult(
                mockMvc.perform(patch(BASE_URL + "/change-status")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }

    @Then("the response should contain trainer first name {string}")
    public void the_response_should_contain_trainer_first_name(String name) throws Exception {
        String content = sharedContext.getResult().getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(name));
    }

    @When("the client updates trainer profile for {string} with new first name {string}")
    public void the_client_updates_trainer_profile_for_with_new_first_name(String ignored, String newFirstName) throws Exception {
        String username = sharedContext.get("trainerUsername", String.class);

        UpdateTrainerProfileRequest request = new UpdateTrainerProfileRequest();
        request.setUsername(username);
        request.setFirstName(newFirstName);
        request.setLastName("Updated");
        request.setIsActive(true);
        request.setSpecialization(new SpecializationName("Fitness"));

        sharedContext.setResult(
                mockMvc.perform(put(BASE_URL + "/update-profile")
                                .header("Authorization", authSteps.getJwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andReturn()
        );
    }



}
