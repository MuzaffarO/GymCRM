package epam.gymcrm.health;

import epam.gymcrm.repository.TraineeRepository;
import epam.gymcrm.model.Trainee;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TraineeTrainerLinkHealthIndicator implements HealthIndicator {

    private final TraineeRepository traineeRepository;

    public TraineeTrainerLinkHealthIndicator(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    @Override
    public Health health() {
        long unlinkedTrainees = traineeRepository.findAll().stream()
                .filter(t -> t.getTrainers() == null || t.getTrainers().isEmpty())
                .count();

        if (unlinkedTrainees == 0) {
            return Health.up().withDetail("trainee-trainer-links", "All trainees linked to trainers").build();
        } else {
            return Health.down().withDetail("trainee-trainer-links", unlinkedTrainees + " trainees have no trainer").build();
        }
    }
}
