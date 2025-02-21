package epam.gymcrm.storage;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import epam.gymcrm.model.TrainingType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Component
public class Storage {
    @Getter
    private final Map<String, Trainee> trainees = new HashMap<>();
    @Getter
    private final Map<String, Trainer> trainers = new HashMap<>();
    @Getter
    private final Map<String, List<Training>> trainings = new HashMap<>();

    private String dataFilePath;

    @Value(value = "${storage.file.path}")
    public void setDataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
    }

    @PostConstruct
    public void initializeStorage() {
        log.info("Initializing Storage from file: {}", dataFilePath);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(dataFilePath)))) {
            if (reader == null) {
                log.error("Error: Data file not found at {}", dataFilePath);
                return;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length < 6) {
                    log.warn("Skipping invalid line (missing fields): {}", line);
                    continue;
                }

                if (data[0].equalsIgnoreCase("Trainee")) {
                    if (data.length < 8) {
                        log.warn("Skipping invalid Trainee entry: {}", line);
                        continue;
                    }

                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String username = data[3].trim();
                    String password = data[4].trim();
                    boolean isActive = Boolean.parseBoolean(data[5].trim());
                    String dateOfBirth = data[6].trim();
                    String address = data[7].trim();

                    Trainee trainee = new Trainee(firstName, lastName, username, password, isActive, dateOfBirth, address);
                    trainees.put(username, trainee);
                    log.info("Loaded Trainee: {}", trainee.getUsername());

                } else if (data[0].equalsIgnoreCase("Trainer")) {
                    if (data.length < 7) {
                        log.warn("Skipping invalid Trainer entry: {}", line);
                        continue;
                    }

                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String username = data[3].trim();
                    String password = data[4].trim();
                    boolean isActive = Boolean.parseBoolean(data[5].trim());
                    String specializationName = data[6].trim();
                    TrainingType specialization = new TrainingType(specializationName);

                    Trainer trainer = new Trainer(firstName, lastName, username, password, isActive, specialization);
                    trainers.put(username, trainer);
                    log.info("Loaded Trainer: {}", trainer.getUsername());
                }
            }
        } catch (Exception e) {
            log.error("Error loading data from file: {}", dataFilePath, e);
        }
    }
}
