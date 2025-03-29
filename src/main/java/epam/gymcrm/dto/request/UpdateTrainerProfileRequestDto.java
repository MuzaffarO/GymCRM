package epam.gymcrm.dto.request;

import epam.gymcrm.dto.response.SpecializationNameDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerProfileRequestDto {

    @Schema(description = "Username of the trainer to be updated", example = "jason.john", required = true)
    @NotBlank
    private String username;

    @Schema(description = "Updated first name of the trainer", example = "jason", required = true)
    @NotBlank
    private String firstName;

    @Schema(description = "Updated last name of the trainer", example = "john", required = true)
    @NotBlank
    private String lastName;

    @Schema(description = "Updated specialization of the trainer", example = "{\"trainingTypeName\": \"soccer\"}")
    @NotNull
    private SpecializationNameDto specialization;

    @Schema(description = "Whether the trainer is active or not", example = "true", required = true)
    @NotNull
    private Boolean isActive;
}
