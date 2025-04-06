package ru.otus.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.dao.Dao;
import ru.otus.hw.dao.AddresseeDaoImpl;
import ru.otus.hw.domain.Addressee;

@Configuration
@EnableConfigurationProperties(PostOfficeProperties.class)
public class PostOfficeConfig {
    @Bean
    public Dao<Addressee> addresseeDao() {
        return new AddresseeDaoImpl();
    }
}
