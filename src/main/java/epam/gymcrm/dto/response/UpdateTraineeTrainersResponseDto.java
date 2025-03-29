package epam.gymcrm.dto.response;

import java.util.List;

public record UpdateTraineeTrainersResponseDto(
        List<TrainerResponseDto> trainersList
) {
}
