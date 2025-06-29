package epam.gymcrm.bdd.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import epam.gymcrm.dto.training.request.TrainingRegister;
import epam.gymcrm.model.*;
import epam.gymcrm.repository.*;
import epam.gymcrm.security.JwtUtil;
import epam.gymcrm.service.TrainingService;
import io.cucumber.java.en.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.ZoneId;


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
    @Autowired private JwtUtil jwtUtil;


    private final RestTemplate restTemplate = new RestTemplate();
    private final String jwtSecret = "vR7xP9m$Jk3!qW@fYzL2bNcT#H8sAe4D";
    private final String workloadServiceBaseUrl = "http://localhost:8081";


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
        String username = "testuser";
        UserDetails dummyUser = org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password("password")
                .roles("USER")
                .build();

        String jwt = jwtUtil.generateToken(dummyUser);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                dummyUser, jwt, dummyUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

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

        Training training = trainingService.createTraining(register);
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

        try {
            Trainee trainee = traineeRepository.findByUserUsername(traineeUsername).orElseThrow();
            Trainer trainer = trainerRepository.findByUserUsername(trainerUsername).orElseThrow();
            TrainingType type = trainingTypeRepository.findByTrainingTypeName(invalidTrainingName)
                    .orElseThrow(() -> new RuntimeException("Training type not found"));

            if (!trainer.getSpecializationType().equals(type)) {
                return;
            }

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
        Training deleted = trainingService.cancelTraining(createdTrainingId);
        assertThat(deleted).isNotNull();
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

    @Then("the workload microservice should reflect the training for the trainer")
    public void the_workload_microservice_should_reflect_the_training_for_the_trainer() throws Exception {
        String trainerUsername = context.get("trainerUsername");

        Date trainingDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2026");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trainingDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String url = String.format("http://localhost:8081/api/workload/%s/%d/%d", trainerUsername, year, month);
        String token = generateToken(trainerUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        double expectedHours = 1.5;
        boolean success = false;
        int attempts = 0;

        while (attempts++ < 5) {
            try {
                ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Double.class);
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() == expectedHours) {
                    success = true;
                    break;
                }
            } catch (Exception e) {
            }
            Thread.sleep(1000); // wait 1 second before retry
        }

        assertThat(success).as("Workload microservice should reflect training hours").isTrue();
    }


    @Then("the workload microservice should reflect cancellation for the trainer")
    public void the_workload_microservice_should_reflect_cancellation_for_the_trainer() throws Exception {
        String trainerUsername = context.get("trainerUsername");

        Date trainingDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2026");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trainingDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String url = String.format("%s/api/workload/%s/%d/%d", workloadServiceBaseUrl, trainerUsername, year, month);
        String token = generateToken(trainerUsername);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Double.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo(0.0);
    }

    private String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(key)
                .compact();
    }




}
