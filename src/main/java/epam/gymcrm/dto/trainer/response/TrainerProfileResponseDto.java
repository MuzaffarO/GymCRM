package epam.gymcrm.dto.trainer.response;

import epam.gymcrm.dto.user.request.SpecializationNameDto;
import epam.gymcrm.dto.trainee.response.TraineeResponseDto;

import java.util.List;

public record TrainerProfileResponseDto(

        String firstName,
        String lastName,
        SpecializationNameDto specialization,
        Boolean isActive,
        List<TraineeResponseDto> traineeResponseDto
) {
}
