package epam.gymcrm.main;

import epam.gymcrm.config.AppConfig;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.GymFacade;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.List;

@Slf4j
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        log.info("Spring Application Started Successfully!");

        GymFacade gymFacade = context.getBean(GymFacade.class);

        log.info("🔹 Testing TraineeService...");
        Trainee trainee1 = new Trainee("John", "Doe", "john.doe", "securePass", true, "1995-08-15", "NY, USA");
        Trainee trainee2 = new Trainee("Alice", "Brown", "alice.brown", "pass123", true, "1998-03-12", "LA, USA");

        gymFacade.getTraineeService().registerTrainee(trainee1);
        gymFacade.getTraineeService().registerTrainee(trainee2);

        Optional<Trainee> retrievedTrainee = gymFacade.getTraineeService().selectTrainee("john.doe");
        retrievedTrainee.ifPresent(t -> log.info("Selected Trainee: {} - {}", t.getUsername(), t.getAddress()));

        log.info("Total Trainees: {}", gymFacade.getTraineeService().getAllTrainees().size());

        gymFacade.getTraineeService().deleteTrainee("alice.brown");
        log.info("After deletion, Total Trainees: {}", gymFacade.getTraineeService().getAllTrainees().size());

        log.info("Testing TrainerService...");
        Trainer trainer1 = new Trainer("Bob", "Smith", "bob.smith", "trainerPass", true, new TrainingType("Java Programming"));
        Trainer trainer2 = new Trainer("Sophia", "Jones", "sophia.jones", "trainerSecure", true, new TrainingType("Data Science"));

        gymFacade.getTrainerService().registerTrainer(trainer1);
        gymFacade.getTrainerService().registerTrainer(trainer2);

        Optional<Trainer> retrievedTrainer = gymFacade.getTrainerService().selectTrainer("bob.smith");
        retrievedTrainer.ifPresent(t -> log.info("Selected Trainer: {} - Specialization: {}", t.getUsername(), t.getSpecialization().getTrainingTypeName()));

        log.info("Total Trainers: {}", gymFacade.getTrainerService().getAllTrainers().size());

        log.info("Testing TrainingService...");
        Training training1 = new Training(trainee1, trainer1, "Java Basics", new TrainingType("Java Programming"), null, 5);
        Training training2 = new Training(trainee1, trainer2, "Data Science Bootcamp", new TrainingType("Data Science"), null, 10);

        gymFacade.getTrainingService().createTraining(training1);
        gymFacade.getTrainingService().createTraining(training2);

        log.info("Total Trainings: {}", gymFacade.getTrainingService().getAllTrainings().size());

        List<Training> javaTrainings = gymFacade.getTrainingService().selectTraining("Java Basics");
        log.info("Trainings Found for 'Java Basics': {}", javaTrainings.size());

        log.info("Application Execution Completed Successfully!");
    }
}
