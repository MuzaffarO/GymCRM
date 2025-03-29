package epam.gymcrm.dto.response;

import lombok.Getter;

import java.util.List;

public record UpdateTraineeTrainersResponseDto(
        List<TrainerResponseDto> trainersList
) {
}
