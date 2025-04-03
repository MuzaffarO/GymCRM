package epam.gymcrm.dto.trainee.response;

import epam.gymcrm.dto.trainer.response.TrainerResponseDto;

import java.util.List;

public record UpdateTraineeTrainersResponseDto(
        List<TrainerResponseDto> trainersList
) {
}
