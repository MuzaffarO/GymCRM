package epam.gymcrm.dao;

import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import jakarta.validation.constraints.NotBlank;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerDao extends CRUDDao<Trainer, Integer> {

    List<Training> getTrainerTrainingsByUsername(String username);

    Optional<Trainer> findByUserUsername(String username);

    Collection<Trainer> findNotAssignedActiveTrainers(String username);

    List<Training> findTrainerTrainingsByFilters(String username, Date periodFrom, Date periodTo, String traineeName);
}
