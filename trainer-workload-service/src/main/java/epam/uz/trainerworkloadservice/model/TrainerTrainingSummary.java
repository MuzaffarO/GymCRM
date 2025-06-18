package epam.uz.trainerworkloadservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "trainer_summaries")
@CompoundIndex(def = "{'firstName': 1, 'lastName': 1}")
public class TrainerTrainingSummary {

    @Id
    private String id;

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean active;

    @Valid
    private List<YearlySummary> yearlySummaries = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class YearlySummary {
        @Min(1900)
        private int year;

        @Valid
        private List<MonthlySummary> monthlySummaries = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlySummary {
        @Min(1) @Max(12)
        private int month;

        @Min(0)
        private double trainingsSummaryDuration;
    }
}
