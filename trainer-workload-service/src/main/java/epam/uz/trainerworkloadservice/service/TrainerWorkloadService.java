package epam.uz.trainerworkloadservice.service;

import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.model.MonthlySummary;
import epam.uz.trainerworkloadservice.model.TrainerSummary;
import epam.uz.trainerworkloadservice.repository.MonthlySummaryRepository;
import epam.uz.trainerworkloadservice.repository.TrainerSummaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final TrainerSummaryRepository trainerRepo;
    private final MonthlySummaryRepository summaryRepo;

    @Transactional
    public void processWorkload(TrainerWorkloadRequest request) {
        TrainerSummary trainer = trainerRepo.findByTrainerUsername(request.getTrainerUsername())
                .orElseGet(() -> trainerRepo.save(TrainerSummary.builder()
                        .trainerUsername(request.getTrainerUsername())
                        .firstName(request.getTrainerFirstName())
                        .lastName(request.getTrainerLastName())
                        .isActive(request.isActive())
                        .build()));

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();
        double duration = request.getTrainingDuration();

        MonthlySummary summary = summaryRepo.findByTrainerAndYearAndMonth(trainer, year, month)
                .orElse(MonthlySummary.builder()
                        .trainer(trainer)
                        .year(year)
                        .month(month)
                        .totalHours(0.0)
                        .build());

        double updatedHours = "ADD".equalsIgnoreCase(String.valueOf(request.getActionType()))
                ? summary.getTotalHours() + duration
                : Math.max(0.0, summary.getTotalHours() - duration);

        summary.setTotalHours(updatedHours);
        summaryRepo.save(summary);
    }
    public TrainerSummary getTrainerByUsername(String username) {
        return trainerRepo.findByTrainerUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + username));
    }

    public MonthlySummary getMonthlySummary(TrainerSummary trainer, int year, int month) {
        return summaryRepo.findByTrainerAndYearAndMonth(trainer, year, month)
                .orElseThrow(() -> new RuntimeException("No summary found for the specified month and year"));
    }

    public TrainerMonthlySummaryDTO getTrainerMonthlySummary(String username) {
        TrainerSummary trainer = trainerRepo.findByTrainerUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        List<MonthlySummary> summaries = summaryRepo.findAll()
                .stream()
                .filter(s -> s.getTrainer().equals(trainer))
                .toList();

        Map<Integer, List<TrainerMonthlySummaryDTO.MonthlySummary>> byYear = summaries.stream()
                .collect(Collectors.groupingBy(
                        MonthlySummary::getYear,
                        Collectors.mapping(
                                s -> new TrainerMonthlySummaryDTO.MonthlySummary(s.getMonth(), s.getTotalHours()),
                                Collectors.toList()
                        )
                ));

        List<TrainerMonthlySummaryDTO.YearlySummary> yearlySummaries = byYear.entrySet().stream()
                .map(e -> new TrainerMonthlySummaryDTO.YearlySummary(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new TrainerMonthlySummaryDTO(
                trainer.getTrainerUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.isActive(),
                yearlySummaries
        );
    }

}
