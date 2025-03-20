package epam.gymcrm.dao;

import epam.gymcrm.model.Training;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface TrainingDao extends CRUDDao<Training, Integer> {

    List<Training> findTraineeTrainingsByFilters(String username, Date periodFrom, Date periodTo, String trainerName, String trainingType);
}
