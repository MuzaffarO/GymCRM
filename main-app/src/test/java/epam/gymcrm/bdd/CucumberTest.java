package epam.gymcrm.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "epam.gymcrm.bdd.steps",
        plugin = {"pretty", "summary"},
        publish = false
)
public class CucumberTest {
}
