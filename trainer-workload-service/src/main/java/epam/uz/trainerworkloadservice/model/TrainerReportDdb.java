package epam.uz.trainerworkloadservice.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerReportDdb {

    private String trainerUsername;
    private String traineeStatus;
    private String firstName;
    private String lastName;
    private Boolean active;
    private List<YearlySummary> yearlySummaries = new ArrayList<>();

    @DynamoDbPartitionKey
    public String getTrainerUsername() {
        return trainerUsername;
    }

    @DynamoDbSortKey
    public String getTraineeStatus() {
        return traineeStatus;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "TrainerName-Index")
    public String getFirstName() {
        return firstName;
    }

    @DynamoDbSecondarySortKey(indexNames = "TrainerName-Index")
    public String getLastName() {
        return lastName;
    }

    public void setActive(Boolean a) {
        this.active = a;
        this.traineeStatus = (a != null && a) ? "Active" : "Inactive";
    }

    public void setTraineeStatus(String s) {
        this.traineeStatus = s;
        this.active = "Active".equalsIgnoreCase(s);
    }


    @DynamoDbBean
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class YearlySummary {
        private int year;
        private List<MonthlySummary> monthlySummaries = new ArrayList<>();
    }

    @DynamoDbBean
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MonthlySummary {
        private int month;
        private double totalHours;
    }
}
