package epam.uz.trainerworkloadservice.repository;

import epam.uz.trainerworkloadservice.model.TrainerTrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;
import java.util.List;

public interface TrainerTrainingSummaryRepository extends MongoRepository<TrainerTrainingSummary, String> {
    Optional<TrainerTrainingSummary> findByTrainerUsername(String trainerUsername);
    List<TrainerTrainingSummary> findByFirstNameAndLastName(String firstName, String lastName);
    List<TrainerTrainingSummary> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
