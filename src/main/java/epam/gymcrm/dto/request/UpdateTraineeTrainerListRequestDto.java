package epam.gymcrm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateTraineeTrainerListRequestDto {

    @Schema(description = "Username of the trainee whose trainer list is being updated", example = "john.doe", required = true)
    @NotBlank
    @NotNull
    private String username;

    @Schema(description = "List of trainer usernames to assign to the trainee", required = true)
    @NotEmpty
    @Valid
    private List<@Valid TrainerUsernameRequestDto> trainersList;
}
