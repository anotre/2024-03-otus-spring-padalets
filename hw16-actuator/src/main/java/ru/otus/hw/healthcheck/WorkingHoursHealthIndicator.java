package ru.otus.hw.healthcheck;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
public class WorkingHoursHealthIndicator implements HealthIndicator {
    public Health health() {
        var healthCheck = this.check();

        if (healthCheck) {
            return Health.down().withDetail("Door note:", "Off hours! Visit us tomorrow.").build();
        }

        return Health.up().build();
    }

    public boolean check() {
        var currentTime = LocalTime.now();
        var highTimeThreshold = LocalTime.of(9, 0, 0);
        var lowTimeThreshold = LocalTime.of(18, 0, 0);

        return currentTime.isAfter(lowTimeThreshold) && currentTime.isBefore(highTimeThreshold);
    }
}
