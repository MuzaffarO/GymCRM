package epam.uz.trainerworkloadservice.repository;

import epam.uz.trainerworkloadservice.model.MonthlySummary;
import epam.uz.trainerworkloadservice.model.TrainerSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlySummaryRepository extends JpaRepository<MonthlySummary, Long> {
    Optional<MonthlySummary> findByTrainerAndYearAndMonth(TrainerSummary trainer, int year, int month);
}
