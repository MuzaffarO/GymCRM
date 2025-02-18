package epam.gymcrm.service;

import epam.gymcrm.model.Trainee;

import java.util.List;

public interface TraineeService {
    void registerTrainee(Trainee trainee);
//    void selectTrainee();
    List<Trainee> getAllTrainees();
}

