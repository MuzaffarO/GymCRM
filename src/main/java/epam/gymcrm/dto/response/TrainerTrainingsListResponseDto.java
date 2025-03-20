package epam.gymcrm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record TrainerTrainingsListResponseDto (
        String trainingName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        Date trainingDate,
        String trainingType,
        Number trainingDuration,
        String traineeName
){
}
