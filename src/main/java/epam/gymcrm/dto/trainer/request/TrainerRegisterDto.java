package epam.gymcrm.dto.trainer.request;

import epam.gymcrm.dto.trainingtype.TrainingTypeDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerRegisterDto {

    @Schema(description = "Trainer's first name", example = "John", required = true)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Schema(description = "Trainer's last name", example = "Doe", required = true)
    @NotBlank(message = "Last name cannot be blank")
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Schema(description = "Trainer's specialization type",example = "soccer", required = true)
    @Valid
    @NotNull(message = "Specialization cannot be null")
    private TrainingTypeDto specialization;
}
