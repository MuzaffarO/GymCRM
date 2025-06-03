package epam.gymcrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GymCRMApplication {
    public static void main(String[] args) {
        SpringApplication.run(GymCRMApplication.class, args);
    }
}
