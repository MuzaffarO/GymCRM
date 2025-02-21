package epam.gymcrm.service;

import epam.gymcrm.model.Training;
import java.util.List;

public interface TrainingService {
    void createTraining(Training training);
    List<Training> selectTraining(String trainingName);
    List<Training> getAllTrainings();
}


