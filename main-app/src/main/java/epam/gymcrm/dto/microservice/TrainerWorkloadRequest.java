package epam.gymcrm.dto.microservice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerWorkloadRequest {
    @NotBlank
    private String trainerUsername;
    @NotBlank
    private String trainerFirstName;
    @NotBlank
    private String trainerLastName;
    private boolean isActive;
    @NotNull
    private LocalDate trainingDate;
    @Min(0)
    private double trainingDuration;
    @NotNull
    private ActionType actionType;
}
