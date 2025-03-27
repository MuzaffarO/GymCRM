package epam.gymcrm.repository;

import epam.gymcrm.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer> {
    Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
    
}
