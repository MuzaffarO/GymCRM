package epam.gymcrm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record UpdateTraineeProfileResponseDto(
        String username,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerResponseDto> trainersList
) {
}
