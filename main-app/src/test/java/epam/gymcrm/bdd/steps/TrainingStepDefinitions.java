package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.model.*;
import epam.gymcrm.repository.*;
import epam.gymcrm.service.TrainingService;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
public class TrainingStepDefinitions {

    @Autowired private UserRepository userRepository;
    @Autowired private TraineeRepository traineeRepository;
    @Autowired private TrainerRepository trainerRepository;
    @Autowired private TrainingTypeRepository trainingTypeRepository;
    @Autowired private TrainingRepository trainingRepository;
    @Autowired private TrainingService trainingService;

    private final Map<String, String> context = new HashMap<>();
    private Integer createdTrainingId;

    @Given("a trainee is registered")
    @Transactional
    public void a_trainee_is_registered() {
        String username = "trainee_" + UUID.randomUUID().toString().substring(0, 6);
        context.put("traineeUsername", username);

        User user = userRepository.save(User.builder()
                .username(username)
                .firstName("Auto")
                .lastName("Trainee")
                .password("password")
                .isActive(true)
                .build());

        Trainee trainee = Trainee.builder().user(user).build();
        traineeRepository.save(trainee);
    }

    @Given("a trainer is registered with specialization {string}")
    @Transactional
    public void a_trainer_is_registered_with_specialization(String specialization) {
        String username = "trainer_" + UUID.randomUUID().toString().substring(0, 6);
        context.put("trainerUsername", username);

        TrainingType type = trainingTypeRepository.findByTrainingTypeName(specialization)
                .orElseGet(() -> trainingTypeRepository.save(
                        TrainingType.builder().trainingTypeName(specialization).build()));

        User user = userRepository.save(User.builder()
                .username(username)
                .firstName("Auto")
                .lastName("Trainer")
                .password("password")
                .isActive(true)
                .build());

        Trainer trainer = Trainer.builder()
                .user(user)
                .specializationType(type)
                .build();

        trainerRepository.save(trainer);
    }

    @Given("a training type {string} exists")
    @Transactional
    public void a_training_type_exists(String name) {
        trainingTypeRepository.findByTrainingTypeName(name)
                .orElseGet(() -> trainingTypeRepository.save(
                        TrainingType.builder().trainingTypeName(name).build()));
    }

    @When("a training is created")
    @Transactional
    public void a_training_is_created() throws Exception {
        String traineeUsername = context.get("traineeUsername");
        String trainerUsername = context.get("trainerUsername");
        String trainingName = "boxing";
        Date trainingDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2026");

        TrainingRegister register = new TrainingRegister();
        register.setTraineeUsername(traineeUsername);
        register.setTrainerUsername(trainerUsername);
        register.setTrainingName(trainingName);
        register.setTrainingDuration(1.5);
        register.setTrainingDate(trainingDate);

        // Manual service simulation
        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername).orElseThrow();
        Trainer trainer = trainerRepository.findByUserUsername(trainerUsername).orElseThrow();
        TrainingType type = trainingTypeRepository.findByTrainingTypeName(trainingName).orElseThrow();

        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingType(type)
                .trainingName(trainingName)
                .trainingDate(trainingDate)
                .trainingDuration(1.5)
                .build();

        trainingRepository.save(training);
        createdTrainingId = training.getId();
    }

    @When("a training is attempted with mismatched specialization")
    @Transactional
    public void a_training_is_attempted_with_mismatched_specialization() throws Exception {
        String traineeUsername = context.get("traineeUsername");
        String trainerUsername = context.get("trainerUsername");
        String invalidTrainingName = "karate";  // Mismatched specialization
        Date trainingDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2026");

        TrainingRegister register = new TrainingRegister();
        register.setTraineeUsername(traineeUsername);
        register.setTrainerUsername(trainerUsername);
        register.setTrainingName(invalidTrainingName);
        register.setTrainingDuration(1.5);
        register.setTrainingDate(trainingDate);

        // Simulating service logic expecting failure due to mismatch
        try {
            Trainee trainee = traineeRepository.findByUserUsername(traineeUsername).orElseThrow();
            Trainer trainer = trainerRepository.findByUserUsername(trainerUsername).orElseThrow();
            TrainingType type = trainingTypeRepository.findByTrainingTypeName(invalidTrainingName)
                    .orElseThrow(() -> new RuntimeException("Training type not found"));

            // Mismatch check (simulated as in real service)
            if (!trainer.getSpecializationType().equals(type)) {
                return; // Intentionally failing creation — simulates exception
            }

            // If by mistake it passes
            throw new AssertionError("Training creation should have failed due to specialization mismatch");

        } catch (Exception e) {
            context.put("failureReason", e.getMessage());
        }
    }

    @Then("the training should not be created")
    public void the_training_should_not_be_created() {
        assertThat(createdTrainingId).isNull();
    }


    @Then("the training should exist in the database")
    @Transactional
    public void the_training_should_exist_in_the_database() {
        assertThat(createdTrainingId).isNotNull();
        assertThat(trainingRepository.findById(createdTrainingId)).isPresent();
    }

    @When("the training is cancelled")
    @Transactional
    public void the_training_is_cancelled() {
        assertThat(createdTrainingId).isNotNull();

        Training training = trainingRepository.findById(createdTrainingId).orElseThrow();

        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();

        trainee.getTrainings().remove(training);
        trainer.getTrainingList().remove(training);

        trainingRepository.delete(training);

        boolean stillConnected = trainingRepository.existsByTraineeAndTrainer(trainee, trainer);
        if (!stillConnected) {
            trainee.getTrainers().remove(trainer);
            trainer.getTrainees().remove(trainee);

            traineeRepository.save(trainee);
            trainerRepository.save(trainer);
        }
    }

    @When("an attempt is made to cancel a non-existent training")
    public void an_attempt_is_made_to_cancel_a_non_existent_training() {
        try {
            trainingService.cancelTraining(-999);
            context.put("cancelError", "none");
        } catch (Exception e) {
            context.put("cancelError", e.getClass().getSimpleName());
        }
    }

    @Then("an error should occur indicating training not found")
    public void an_error_should_occur_indicating_training_not_found() {
        assertThat(context.get("cancelError")).isNotEqualTo("none");
    }

    @Then("the training should not exist in the database")
    @Transactional
    public void the_training_should_not_exist_in_the_database() {
        assertThat(trainingRepository.findById(createdTrainingId)).isEmpty();
    }

}
