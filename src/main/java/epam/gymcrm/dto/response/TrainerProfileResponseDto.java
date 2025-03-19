package epam.gymcrm.dto.response;

import java.util.List;

public record TrainerProfileResponseDto(

        String firstName,
        String lastName,
        SpecializationNameDto specialization,
        Boolean isActive,
        List<TraineeResponseDto> traineeResponseDto
) {
}
