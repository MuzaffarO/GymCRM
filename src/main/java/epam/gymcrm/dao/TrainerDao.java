package epam.gymcrm.dao;

import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;

import java.util.List;
import java.util.Optional;

public interface TrainerDao extends CRUDDao<Trainer, Integer> {

    List<Training> getTrainerTrainingsByUsername(String username);

    Optional<Trainer> findByUserUsername(String username);

    List<Trainer> findNotAssignedTrainers();

}
