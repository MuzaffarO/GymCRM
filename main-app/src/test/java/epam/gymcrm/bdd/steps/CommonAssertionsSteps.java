package epam.gymcrm.bdd.steps;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonAssertionsSteps {

    @Autowired
    private SharedContext sharedContext;

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        Assertions.assertEquals(status, sharedContext.getResult().getResponse().getStatus());
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String message) throws Exception {
        String content = sharedContext.getResult().getResponse().getContentAsString();
        Assertions.assertTrue(content.contains(message));
    }
}
