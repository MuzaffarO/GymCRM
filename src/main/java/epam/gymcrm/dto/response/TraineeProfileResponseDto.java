package epam.gymcrm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record TraineeProfileResponseDto(

        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerResponseDto> trainersList
) {
}