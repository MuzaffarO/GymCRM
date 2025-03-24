package epam.gymcrm.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateTraineeTrainerListRequestDto {
    @NotBlank
    private String username;

    @NotEmpty
    @Valid
    private List<@Valid TrainerUsernameRequestDto> trainersList;
}
