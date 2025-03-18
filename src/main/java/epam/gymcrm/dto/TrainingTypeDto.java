package epam.gymcrm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingTypeDto {
    private Integer id;

    @NotBlank(message = "Training type name cannot be blank")
    private String trainingTypeName;
}
