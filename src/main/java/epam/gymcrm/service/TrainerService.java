package epam.gymcrm.service;

import epam.gymcrm.model.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerService {
    void registerTrainer(Trainer trainer);
    Optional<Trainer> selectTrainer(String username);
    void updateTrainer(String username, Trainer updatedTrainer);
    List<Trainer> getAllTrainers();
}

