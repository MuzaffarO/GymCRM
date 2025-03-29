package epam.gymcrm.health;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    private final JdbcTemplate jdbcTemplate;
    private final MeterRegistry meterRegistry;

    public DatabaseHealthIndicator(JdbcTemplate jdbcTemplate, MeterRegistry meterRegistry) {
        this.jdbcTemplate = jdbcTemplate;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Health health() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                meterRegistry.counter("gymcrm.database.health.success").increment();
                return Health.up().withDetail("database", "PostgreSQL is reachable").build();
            } else {
                meterRegistry.counter("gymcrm.database.health.failure", "reason", "unexpected_result").increment();
                return Health.down().withDetail("database", "Unexpected result from database").build();
            }
        } catch (Exception e) {
            meterRegistry.counter("gymcrm.database.health.failure", "reason", e.getClass().getSimpleName()).increment();
            return Health.down(e).withDetail("database", "PostgreSQL is not reachable").build();
        }
    }
}
