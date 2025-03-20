package epam.gymcrm.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private List<TrainerUsernameRequestDto> trainersList;
}
