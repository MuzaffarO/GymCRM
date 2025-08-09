//package epam.uz.trainerworkloadservice.steps;
//
//import epam.uz.trainerworkloadservice.security.JwtUtil;
//import io.cucumber.java.en.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//public class SecuritySteps {
//
//    @Autowired private JwtUtil jwtUtil;
//    @Autowired private MockMvc mockMvc;
//
//    private HttpHeaders headers = new HttpHeaders();
//    private MvcResult response;
//
//    @Given("a valid JWT token for user {string}")
//    public void a_valid_jwt_token_for_user(String username) {
//        headers.set("Authorization", "Bearer " + jwtUtil.generateToken(username));
//    }
//
//    @Given("an invalid JWT token")
//    public void an_invalid_jwt_token() {
//        headers.set("Authorization", "Bearer fake.invalid.token");
//    }
//
//    @When("the client sends a GET request to {string} with the token")
//    public void the_client_sends_get_with_token(String path) throws Exception {
//        response = mockMvc.perform(get(path)
//                        .header("Authorization", headers.getFirst("Authorization")))
//                .andReturn();
//    }
//
//    @When("the client sends a GET request to {string} without token")
//    public void the_client_sends_get_without_token(String path) throws Exception {
//        response = mockMvc.perform(get(path)).andReturn();
//    }
//
//    @Then("the REST response status should be {int}")
//    public void rest_response_status_should_be(int status) {
//        assertEquals(status, response.getResponse().getStatus());
//    }
//}
