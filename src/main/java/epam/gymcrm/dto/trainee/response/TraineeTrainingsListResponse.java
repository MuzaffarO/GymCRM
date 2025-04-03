package epam.gymcrm.dto.trainee.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TraineeTrainingsListResponse(
        String trainingName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date trainingDate,
        String trainingType,
        Number trainingDuration,
        String trainerName
) {

}
