package epam.gymcrm.storage;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class Storage {
    private final Map<String, List<Trainee>> trainees = new HashMap<>();
    private final Map<String, List<Trainer>> trainers = new HashMap<>();
    private final Map<String, List<Training>> trainings = new HashMap<>();

    public Map<String, List<Trainee>> getTrainees() { return trainees; }
    public Map<String, List<Trainer>> getTrainers() { return trainers; }
    public Map<String, List<Training>> getTrainings() { return trainings; }
}
