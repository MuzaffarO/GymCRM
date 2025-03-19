package epam.gymcrm.dto.response;

import java.util.List;

public record UpdateTrainerProfileResponseDto (
        String username,
        String firstName,
        String lastName,
        SpecializationNameDto specialization,
        Boolean isActive,
        List<TraineeResponseDto> trainees
){
}
