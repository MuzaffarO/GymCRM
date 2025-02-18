package epam.gymcrm.dao;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.storage.Storage;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class TraineeDAO {
    private final Storage storage;

    public TraineeDAO(Storage storage) {
        this.storage = storage;
    }

    public void save(Trainee trainee) {
        storage.getTrainees().computeIfAbsent(trainee.getUsername(), k -> new ArrayList<>()).add(trainee);
    }

    public List<Trainee> findAll() {
        return new ArrayList<>(storage.getTrainees().values().stream().flatMap(List::stream).toList());
    }
}

