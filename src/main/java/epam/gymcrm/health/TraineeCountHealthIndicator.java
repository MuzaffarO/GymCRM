package epam.gymcrm.health;

import epam.gymcrm.repository.TraineeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeCountHealthIndicator implements HealthIndicator {

    private final TraineeRepository traineeRepository;

    @Override
    public Health health() {
        long count = traineeRepository.count();
        if (count > 0) {
            return Health.up().withDetail("trainees", "Trainees present: " + count).build();
        } else {
            return Health.down().withDetail("trainees", "No trainees found").build();
        }
    }
}
