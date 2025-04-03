package epam.gymcrm.dto.trainee.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import epam.gymcrm.dto.trainer.response.TrainerResponse;

import java.util.Date;
import java.util.List;

public record UpdateTraineeProfileResponse(
        String username,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerResponse> trainersList
) {
}
