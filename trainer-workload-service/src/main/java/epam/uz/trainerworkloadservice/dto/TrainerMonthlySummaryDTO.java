package epam.uz.trainerworkloadservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class TrainerMonthlySummaryDTO {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean active;
    private List<YearlySummary> yearlySummaries;

    @Data
    @AllArgsConstructor
    public static class YearlySummary {
        private int year;
        private List<MonthlySummary> monthlySummaries;
    }

    @Data
    @AllArgsConstructor
    public static class MonthlySummary {
        private int month;
        private double totalHours;
    }
}
