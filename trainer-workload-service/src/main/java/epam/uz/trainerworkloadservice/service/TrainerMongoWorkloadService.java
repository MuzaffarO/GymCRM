package epam.uz.trainerworkloadservice.service;
import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.exception.SummaryNotFoundException;
import epam.uz.trainerworkloadservice.model.TrainerTrainingSummary;
import epam.uz.trainerworkloadservice.repository.TrainerTrainingSummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerMongoWorkloadService {

    private final TrainerTrainingSummaryRepository repository;

    public void processWorkload(TrainerWorkloadRequest request) {
        String txnId = "TXN-" + System.currentTimeMillis(); // In real case, get from header
        log.info("[{}] Processing workload for trainer: {}", txnId, request.getTrainerUsername());

        TrainerTrainingSummary summary = repository.findByTrainerUsername(request.getTrainerUsername())
                .orElseGet(() -> {
                    log.info("[{}] Creating new trainer document", txnId);
                    return TrainerTrainingSummary.builder()
                            .trainerUsername(request.getTrainerUsername())
                            .firstName(request.getTrainerFirstName())
                            .lastName(request.getTrainerLastName())
                            .active(request.isActive())
                            .yearlySummaries(new ArrayList<>())
                            .build();
                });

        LocalDate date = request.getTrainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        double duration = request.getTrainingDuration();

        var yearSummary = summary.getYearlySummaries().stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    var newYear = TrainerTrainingSummary.YearlySummary.builder()
                            .year(year)
                            .monthlySummaries(new ArrayList<>())
                            .build();
                    summary.getYearlySummaries().add(newYear);
                    return newYear;
                });

        var monthSummary = yearSummary.getMonthlySummaries().stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    var newMonth = TrainerTrainingSummary.MonthlySummary.builder()
                            .month(month)
                            .trainingsSummaryDuration(0.0)
                            .build();
                    yearSummary.getMonthlySummaries().add(newMonth);
                    return newMonth;
                });

        double updatedDuration = switch (request.getActionType()) {
            case ADD -> monthSummary.getTrainingsSummaryDuration() + duration;
            case DELETE -> Math.max(0, monthSummary.getTrainingsSummaryDuration() - duration);
        };

        monthSummary.setTrainingsSummaryDuration(updatedDuration);
        repository.save(summary);

        log.info("[{}] Updated duration: {} {} | Year: {} Month: {} New Total: {}",
                txnId, summary.getFirstName(), summary.getLastName(), year, month, monthSummary.getTrainingsSummaryDuration());
    }

    public TrainerMonthlySummaryDTO getTrainerMonthlySummary(String username) {
        TrainerTrainingSummary trainer = repository.findByTrainerUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + username));

        Map<Integer, List<TrainerMonthlySummaryDTO.MonthlySummary>> byYear = trainer.getYearlySummaries().stream()
                .collect(Collectors.toMap(
                        TrainerTrainingSummary.YearlySummary::getYear,
                        y -> y.getMonthlySummaries().stream()
                                .map(m -> new TrainerMonthlySummaryDTO.MonthlySummary(m.getMonth(), m.getTrainingsSummaryDuration()))
                                .collect(Collectors.toList())
                ));

        List<TrainerMonthlySummaryDTO.YearlySummary> yearlySummaries = byYear.entrySet().stream()
                .map(e -> new TrainerMonthlySummaryDTO.YearlySummary(e.getKey(), e.getValue()))
                .toList();

        return new TrainerMonthlySummaryDTO(
                trainer.getTrainerUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getActive(),
                yearlySummaries
        );
    }


    public double getMonthlyHours(String username, int year, int month) {
        TrainerTrainingSummary trainer = repository.findByTrainerUsername(username)
                .orElseThrow(() -> new SummaryNotFoundException("Trainer not found: " + username));

        return trainer.getYearlySummaries().stream()
                .filter(y -> y.getYear() == year)
                .flatMap(y -> y.getMonthlySummaries().stream())
                .filter(m -> m.getMonth() == month)
                .map(TrainerTrainingSummary.MonthlySummary::getTrainingsSummaryDuration)
                .findFirst()
                .orElseThrow(() -> new SummaryNotFoundException("No summary found for year=" + year + ", month=" + month));
    }

}
