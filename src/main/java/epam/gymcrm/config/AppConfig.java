package epam.gymcrm.config;

import epam.gymcrm.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@ComponentScan(basePackages = "epam.gymcrm")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean(initMethod = "initializeStorage")
    public Storage storage() {
        return new Storage();
    }
}


