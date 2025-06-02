package epam.gymcrm.dto.trainee.response;

import epam.gymcrm.dto.trainer.response.TrainerResponse;

import java.util.List;

public record UpdateTraineeTrainersResponse(
        List<TrainerResponse> trainersList
) {
}
