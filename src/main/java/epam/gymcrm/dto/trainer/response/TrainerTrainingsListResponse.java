package epam.gymcrm.dto.trainer.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TrainerTrainingsListResponse(
        String trainingName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date trainingDate,
        String trainingType,
        Number trainingDuration,
        String traineeName
){
}
