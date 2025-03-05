package epam.gymcrm.dao;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Training;

import java.util.List;
import java.util.Optional;

public interface TraineeDao extends CRUDDao<Trainee, Integer> {

    void deleteByUsername(String username, String password);

    Optional<Trainee> findByUserUsername(String username, String password);

    List<Training> getTraineeTrainingsByUsername(String username, String password);
}
