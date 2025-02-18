package epam.gymcrm.main;



import epam.gymcrm.config.AppConfig;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.service.TraineeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);

        log.info("Spring Application Started Successfully!");

        // Register a new Trainee
        Trainee trainee = new Trainee("John", "Doe", null, null, true, "1995-08-15", "NY, USA");
        traineeService.registerTrainee(trainee);

        log.info("Application Execution Completed.");
    }
}

