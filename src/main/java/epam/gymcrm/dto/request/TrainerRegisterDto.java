package epam.gymcrm.dto.request;

import epam.gymcrm.dto.TrainingTypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerRegisterDto {

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Valid
    @NotNull(message = "Specialization cannot be null")
    private TrainingTypeDto specialization;
}
