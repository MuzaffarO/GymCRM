package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegister;
import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainer.request.TrainerRegister;
import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.repository.UserRepository;
import epam.gymcrm.security.JwtUtil;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@RequiredArgsConstructor
public class UserControllerSteps {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private SharedContext sharedContext;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AuthSteps authSteps;

    private final String BASE_URL = "/users";

    @When("the client registers a trainer with first name {string} and last name {string} and specialization {string}")
    public void register_trainer(String firstName, String lastName, String specialization) throws Exception {
        TrainerRegister trainer = new TrainerRegister();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setSpecialization(new TrainingTypeDTO(999,specialization));

        sharedContext.setResult(mockMvc.perform(post(BASE_URL + "/trainer/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andReturn());
    }

    @When("the client registers a trainee with first name {string} and last name {string} and date of birth {string} and address {string}")
    public void register_trainee(String firstName, String lastName, String dob, String address) throws Exception {
        TraineeRegister trainee = new TraineeRegister();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);

        LocalDate localDate = LocalDate.parse(dob); // dob should be in ISO format: yyyy-MM-dd
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        trainee.setDateOfBirth(date);

        trainee.setAddress(address);

        sharedContext.setResult(mockMvc.perform(post(BASE_URL + "/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainee)))
                .andReturn());
    }

    @Given("a registered user with username {string} and password {string}")
    public void create_test_user(String username, String password) {
        authSteps.createTestUser(username, password);
        authSteps.generateTokenFor(username);
    }


    @When("the client attempts to log in with username {string} and password {string}")
    public void login_user(String username, String password) throws Exception {
        LoginRequest login = new LoginRequest(username, password);
        login.setUsername(username);
        login.setPassword(password);

        sharedContext.setResult(mockMvc.perform(post(BASE_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andReturn());
    }

    @When("the client changes password with old password {string} and new password {string}")
    public void change_password(String oldPass, String newPass) throws Exception {
        String username = sharedContext.get("username", String.class);

        authSteps.generateTokenFor(username);

        PasswordChangeRequest req = new PasswordChangeRequest(username, oldPass, newPass);
        System.out.println("Changing " + oldPass + " " + newPass);

        sharedContext.setResult(mockMvc.perform(put(BASE_URL + "/change-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authSteps.getJwt())
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn());
    }

    @When("the client logs out")
    public void logout_user() throws Exception {
        sharedContext.setResult(mockMvc.perform(post(BASE_URL + "/logout")
                .header("Authorization", authSteps.getJwt()))
                .andReturn());
    }

    @When("the client logs out with no token")
    public void logout_no_token() throws Exception {
        sharedContext.setResult(mockMvc.perform(post(BASE_URL + "/logout"))
                .andReturn());
    }
}
