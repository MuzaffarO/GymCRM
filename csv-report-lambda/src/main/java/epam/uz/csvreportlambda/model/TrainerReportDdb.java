package epam.uz.csvreportlambda.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
public class TrainerReportDdb {

    private String trainerUsername;
    private String firstName;
    private String lastName;
    private Boolean active;
    private List<YearlySummary> yearlySummaries;

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<YearlySummary> getYearlySummaries() {
        return yearlySummaries;
    }

    public void setYearlySummaries(List<YearlySummary> yearlySummaries) {
        this.yearlySummaries = yearlySummaries;
    }

    @DynamoDbBean
    public static class YearlySummary {
        private int year;
        private List<MonthlySummary> monthlySummaries;

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public List<MonthlySummary> getMonthlySummaries() {
            return monthlySummaries;
        }

        public void setMonthlySummaries(List<MonthlySummary> monthlySummaries) {
            this.monthlySummaries = monthlySummaries;
        }
    }

    @DynamoDbBean
    public static class MonthlySummary {
        private int month;
        private double totalHours;

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public double getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(double totalHours) {
            this.totalHours = totalHours;
        }
    }
}
