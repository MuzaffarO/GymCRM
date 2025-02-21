package epam.gymcrm.service;

import epam.gymcrm.model.Trainee;
import java.util.List;
import java.util.Optional;

public interface TraineeService {
    void registerTrainee(Trainee trainee);
    Optional<Trainee> selectTrainee(String username);
    void updateTrainee(String username, Trainee updatedTrainee);
    void deleteTrainee(String username);
    List<Trainee> getAllTrainees();
}


