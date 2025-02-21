package epam.gymcrm.dao;

import epam.gymcrm.model.Training;
import epam.gymcrm.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainingDAO {
    @Autowired
    private Storage storage;

    public void save(Training training) {
        storage.getTrainings().computeIfAbsent(training.getTrainingName(), k -> new java.util.ArrayList<>())
                .add(training);
    }

    public List<Training> findAll() {
        return storage.getTrainings().values().stream().flatMap(List::stream).toList();
    }

    public List<Training> findByTrainingName(String trainingName) {
        return storage.getTrainings().getOrDefault(trainingName, List.of());
    }
}
