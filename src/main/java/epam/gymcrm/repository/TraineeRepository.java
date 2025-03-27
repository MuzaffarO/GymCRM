package epam.gymcrm.repository;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer> {
    @Query(value = "SELECT t FROM Trainee t WHERE t.user.username = :username")
    Optional<Trainee> findByUserUsername(String username);

    @Query(value = "SELECT t FROM Training t JOIN t.trainee usr WHERE usr.user.username = :username")
    List<Training> getTraineeTrainingsByUsername(String username);

}

