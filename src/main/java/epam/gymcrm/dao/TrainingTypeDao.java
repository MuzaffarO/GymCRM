package epam.gymcrm.dao;

import epam.gymcrm.model.TrainingType;

import java.util.Optional;

public interface TrainingTypeDao extends CRUDDao<TrainingType, Integer> {
    Optional<TrainingType> findByName(String trainingTypeName);
}
