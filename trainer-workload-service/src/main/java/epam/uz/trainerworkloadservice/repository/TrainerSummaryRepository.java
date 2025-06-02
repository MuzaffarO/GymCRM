package epam.uz.trainerworkloadservice.repository;

import epam.uz.trainerworkloadservice.model.TrainerSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerSummaryRepository extends JpaRepository<TrainerSummary, Long> {
    Optional<TrainerSummary> findByTrainerUsername(String username);
}
