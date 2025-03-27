package epam.gymcrm.repository;

import epam.gymcrm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer>, JpaSpecificationExecutor<Training> {
}
