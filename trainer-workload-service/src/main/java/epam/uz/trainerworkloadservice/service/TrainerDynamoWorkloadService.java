package epam.uz.trainerworkloadservice.service;

import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.dto.TrainerWorkloadRequest;
import epam.uz.trainerworkloadservice.exception.SummaryNotFoundException;
import epam.uz.trainerworkloadservice.model.TrainerReportDdb;
import epam.uz.trainerworkloadservice.repository.TrainerReportDdbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainerDynamoWorkloadService {

    private final TrainerReportDdbRepository repository;

    public void processWorkload(TrainerWorkloadRequest request) {
        String txnId = "TXN-" + System.currentTimeMillis();
        log.info("[{}] Processing workload for trainer: {}", txnId, request.getTrainerUsername());

        TrainerReportDdb item = repository.get(request.getTrainerUsername(), request.isActive())
                .orElseGet(() -> {
                    log.info("[{}] Creating new trainer item", txnId);
                    return TrainerReportDdb.builder()
                            .trainerUsername(request.getTrainerUsername())
                            .firstName(request.getTrainerFirstName())
                            .lastName(request.getTrainerLastName())
                            .yearlySummaries(new ArrayList<>())
                            .build();
                });
        item.setTraineeStatus(request.isActive() ? "Active" : "Inactive");

        LocalDate date = request.getTrainingDate();
        int year = date.getYear();
        int month = date.getMonthValue();
        double duration = request.getTrainingDuration();

        TrainerReportDdb.YearlySummary yearSummary = item.getYearlySummaries().stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseGet(() -> {
                    var y = TrainerReportDdb.YearlySummary.builder()
                            .year(year)
                            .monthlySummaries(new ArrayList<>())
                            .build();
                    item.getYearlySummaries().add(y);
                    return y;
                });

        TrainerReportDdb.MonthlySummary monthSummary = yearSummary.getMonthlySummaries().stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseGet(() -> {
                    var m = TrainerReportDdb.MonthlySummary.builder()
                            .month(month)
                            .totalHours(0.0)
                            .build();
                    yearSummary.getMonthlySummaries().add(m);
                    return m;
                });

        double updated = switch (request.getActionType()) {
            case ADD -> monthSummary.getTotalHours() + duration;
            case DELETE -> Math.max(0, monthSummary.getTotalHours() - duration);
        };
        monthSummary.setTotalHours(updated);

        repository.save(item);

        log.info("[{}] Updated duration: {} {} | Year: {} Month: {} New Total: {}",
                txnId, item.getFirstName(), item.getLastName(), year, month, monthSummary.getTotalHours());
    }

    public TrainerMonthlySummaryDTO getTrainerMonthlySummary(String username, boolean active) {
        TrainerReportDdb item = repository.get(username, active)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + username));

        Map<Integer, List<TrainerMonthlySummaryDTO.MonthlySummary>> byYear =
                item.getYearlySummaries().stream()
                        .collect(Collectors.toMap(
                                TrainerReportDdb.YearlySummary::getYear,
                                y -> y.getMonthlySummaries().stream()
                                        .map(m -> new TrainerMonthlySummaryDTO.MonthlySummary(m.getMonth(), m.getTotalHours()))
                                        .collect(Collectors.toList())
                        ));

        List<TrainerMonthlySummaryDTO.YearlySummary> yearlySummaries = byYear.entrySet().stream()
                .map(e -> new TrainerMonthlySummaryDTO.YearlySummary(e.getKey(), e.getValue()))
                .toList();

        return new TrainerMonthlySummaryDTO(
                item.getTrainerUsername(),
                item.getFirstName(),
                item.getLastName(),
                Boolean.TRUE.equals(item.getActive()),
                yearlySummaries
        );
    }

    public double getMonthlyHours(String username, boolean active, int year, int month) {
        TrainerReportDdb item = repository.get(username, active)
                .orElseThrow(() -> new SummaryNotFoundException("Trainer not found: " + username));

        return item.getYearlySummaries().stream()
                .filter(y -> y.getYear() == year)
                .flatMap(y -> y.getMonthlySummaries().stream())
                .filter(m -> m.getMonth() == month)
                .map(TrainerReportDdb.MonthlySummary::getTotalHours)
                .findFirst()
                .orElseThrow(() -> new SummaryNotFoundException(
                        "No summary found for year=" + year + ", month=" + month));
    }
}
