package epam.gymcrm.dto.request;

import epam.gymcrm.dto.response.SpecializationNameDto;
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
    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private SpecializationNameDto specialization;

    @NotNull
    private Boolean isActive;
}
