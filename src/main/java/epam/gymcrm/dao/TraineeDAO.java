package epam.gymcrm.dao;


import epam.gymcrm.model.Trainee;
import epam.gymcrm.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TraineeDAO {
    @Autowired
    private Storage storage;

    public void save(Trainee trainee) {
        storage.getTrainees().put(trainee.getUsername(), trainee);
    }

    public List<Trainee> findAll() {
        return List.copyOf(storage.getTrainees().values());
    }

    public Optional<Trainee> findByUsername(String username) {
        return Optional.ofNullable(storage.getTrainees().get(username));
    }

    public boolean delete(String username) {
        return storage.getTrainees().remove(username) != null;
    }
}



