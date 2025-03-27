package epam.gymcrm.repository;

import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer>, JpaSpecificationExecutor<Trainer> {

    @Query("SELECT t FROM Training t JOIN t.trainer usr WHERE usr.user.username = :username")
    List<Training> getTrainerTrainingsByUsername(String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.username = :username")
    Optional<Trainer> findByUserUsername(String username);

    @Query("SELECT t FROM Trainer t " +
            "WHERE t.user.isActive = true " +
            "AND t NOT IN (SELECT tt FROM Trainee tr JOIN tr.trainers tt WHERE tr.user.username = :username)")
    List<Trainer> findNotAssignedActiveTrainers(String username);
}

