package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.trainer.request.SpecializationName;
import epam.gymcrm.dto.trainee.response.TraineeResponse;

import java.util.List;
public record UpdateTrainerProfileResponse(
        String username,
        String firstName,
        String lastName,
        SpecializationName specialization,
        Boolean isActive,
        List<TraineeResponse> trainees
){
}
