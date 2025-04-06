package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "post-office.load")
@Getter
public class PostOfficeProperties {
    private final int min;

    private final int max;

    public PostOfficeProperties(int min, int max) {
        this.min = min;
        this.max = max;
    }
}
