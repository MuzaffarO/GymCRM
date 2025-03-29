package epam.gymcrm.health;

import epam.gymcrm.repository.TrainerRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainerCountHealthIndicator implements HealthIndicator {

    private final TrainerRepository trainerRepository;

    public TrainerCountHealthIndicator(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    @Override
    public Health health() {
        long count = trainerRepository.count();
        if (count > 0) {
            return Health.up().withDetail("trainers", "Active trainers found: " + count).build();
        } else {
            return Health.down().withDetail("trainers", "No trainers found in system").build();
        }
    }
}
