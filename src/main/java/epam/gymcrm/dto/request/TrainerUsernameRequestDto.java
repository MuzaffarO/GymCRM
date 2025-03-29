package epam.gymcrm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainerUsernameRequestDto {

    @Schema(description = "Username of the trainer", example = "john.doe", required = true)
    @NotBlank
    private String username;
}
