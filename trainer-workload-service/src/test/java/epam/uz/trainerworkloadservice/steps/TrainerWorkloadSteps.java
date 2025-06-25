package epam.uz.trainerworkloadservice.steps;

import epam.uz.trainerworkloadservice.dto.ActionType;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.service.TrainerMongoWorkloadService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerWorkloadSteps {

    @Autowired
    private TrainerMongoWorkloadService service;

    private String username;
    private int year;
    private int month;
    private TrainerWorkloadRequest request = new TrainerWorkloadRequest();

    @Given("a trainer with username {string}")
    public void a_trainer_with_username(String username) {
        this.username = username;
        request.setTrainerUsername(username);
        request.setTrainerFirstName("John");
        request.setTrainerLastName("Doe");
        request.setActive(true);
    }

    @When("they perform a training on {date} with duration {double} hours")
    public void they_perform_a_training(LocalDate date, double duration) {
        request.setTrainingDate(date);
        request.setTrainingDuration(duration);
        request.setActionType(ActionType.ADD);
        service.processWorkload(request);
    }

    @When("a training on {date} with duration {double} hour is deleted")
    public void a_training_is_deleted(LocalDate date, double duration) {
        request.setTrainingDate(date);
        request.setTrainingDuration(duration);
        request.setActionType(ActionType.DELETE);
        service.processWorkload(request);
    }

    @Then("the total training hours for {string} in June 2025 should be {double}")
    public void verify_training_hours(String username, double expected) {
        double hours = service.getMonthlyHours(username, 2025, 6);
        assertThat(hours).isEqualTo(expected);
    }
}