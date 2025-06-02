package epam.gymcrm.dto.trainingtype;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingTypeDTO {
    private Integer id;

    @NotBlank(message = "Training type name cannot be blank")
    private String trainingTypeName;
}
