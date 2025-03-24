package epam.gymcrm.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerUsernameRequestDto {
    @NotBlank
    private String username;
}
