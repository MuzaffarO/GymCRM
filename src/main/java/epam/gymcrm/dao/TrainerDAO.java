package epam.gymcrm.dao;

import epam.gymcrm.model.Trainer;
import epam.gymcrm.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDAO {
    @Autowired
    private Storage storage;

    public void save(Trainer trainer) {
        storage.getTrainers().put(trainer.getUsername(), trainer);
    }

    public List<Trainer> findAll() {
        return List.copyOf(storage.getTrainers().values());
    }

    public Optional<Trainer> findByUsername(String username) {
        return Optional.ofNullable(storage.getTrainers().get(username));
    }
}



