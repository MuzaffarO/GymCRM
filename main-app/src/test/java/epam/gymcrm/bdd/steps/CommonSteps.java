package epam.gymcrm.bdd.steps;

import io.cucumber.java.en.Then;
import org.junit.jupiter.api.Assertions;
import org.springframework.test.web.servlet.MvcResult;

public abstract class CommonSteps {

    protected MvcResult result;

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        Assertions.assertEquals(status, result.getResponse().getStatus());
    }
}
