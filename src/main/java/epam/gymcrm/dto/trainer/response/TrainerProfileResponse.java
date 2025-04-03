package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.user.request.SpecializationName;
import epam.gymcrm.dto.trainee.response.TraineeResponse;

import java.util.List;

public record TrainerProfileResponse(

        String firstName,
        String lastName,
        SpecializationName specialization,
        Boolean isActive,
        List<TraineeResponse> traineeResponse
) {
}
