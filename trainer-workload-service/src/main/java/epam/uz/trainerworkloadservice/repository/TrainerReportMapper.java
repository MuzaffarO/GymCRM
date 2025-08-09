package epam.uz.trainerworkloadservice.repository;

import epam.uz.trainerworkloadservice.dto.TrainerMonthlySummaryDTO;
import epam.uz.trainerworkloadservice.model.TrainerReportDdb;

import java.util.List;

public final class TrainerReportMapper {
    private TrainerReportMapper() {
    }

    public static TrainerReportDdb toDdb(TrainerMonthlySummaryDTO dto) {
        var d = new TrainerReportDdb();
        d.setTrainerUsername(dto.getTrainerUsername());
        d.setFirstName(dto.getFirstName());
        d.setLastName(dto.getLastName());
        d.setActive(dto.isActive()); // sets traineeStatus
        var ys = dto.getYearlySummaries() == null ? List.<TrainerReportDdb.YearlySummary>of()
                : dto.getYearlySummaries().stream().map(y ->
                TrainerReportDdb.YearlySummary.builder()
                        .year(y.getYear())
                        .monthlySummaries(y.getMonthlySummaries().stream()
                                .map(m -> TrainerReportDdb.MonthlySummary.builder()
                                        .month(m.getMonth())
                                        .totalHours(m.getTotalHours())
                                        .build())
                                .toList())
                        .build()
        ).toList();
        d.setYearlySummaries(ys);
        return d;
    }
}
