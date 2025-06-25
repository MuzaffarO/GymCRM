package epam.uz.trainerworkloadservice.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.security.JwtUtil;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TrainerWorkloadNegativeSteps {

    @Autowired private MockMvc mockMvc;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private ObjectMapper objectMapper;

    private MvcResult response;
    private String invalidJwt;

    @When("they request summary for {string} in June 2025")
    public void requestSummaryForUsername(String username) throws Exception {
        String token = jwtUtil.generateToken("test-user");
        response = mockMvc.perform(get("/api/workload/" + username + "/2025/6")
                .header("Authorization", "Bearer " + token)).andReturn();
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int expectedStatus) {
        int actualStatus = response.getResponse().getStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    @And("the error message should contain {string}")
    public void verifyErrorMessage(String expectedMessage) throws Exception {
        String content = response.getResponse().getContentAsString();
        System.out.println("Actual response body: " + content);
        assertTrue(content.contains(expectedMessage), "Expected message to contain: " + expectedMessage);
    }

    @Given("a malformed JWT token")
    public void aMalformedJwtToken() {
        this.invalidJwt = "Bearer totallyInvalid###";
    }

    @When("a training workload is submitted")
    public void aTrainingWorkloadIsSubmitted() throws Exception {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder()
                .trainerUsername("john.doe")
                .trainerFirstName("John")
                .trainerLastName("Doe")
                .isActive(true)
                .trainingDate(LocalDate.of(2025, 6, 17))
                .trainingDuration(1.5)
                .actionType(ActionType.ADD)
                .build();

        response = mockMvc.perform(post("/api/workload")
                        .header("Authorization", invalidJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
    }

    @When("they request summary for {string} in February 2027")
    public void theyRequestSummaryForMonthWithNoData(String username) throws Exception {
        String token = "Bearer " + jwtUtil.generateToken(username);
        response = mockMvc.perform(get("/api/workload/" + username + "/2027/2")
                .header("Authorization", token)).andReturn();
    }

}

