package epam.gymcrm.dto.training.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingRegister {

    @Schema(description = "Username of the trainee who will attend the training", example = "alice.smith", required = true)
    @NotBlank
    private String traineeUsername;

    @Schema(description = "Username of the trainer conducting the training", example = "muzaffar.obidjonov", required = true)
    @NotBlank
    private String trainerUsername;

    @Schema(description = "Name of the training", example = "soccer", required = true)
    @NotBlank
    private String trainingName;

    @Schema(description = "Date of the training session (format: dd/MM/yyyy)", example = "01/04/2025", required = true, type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private Date trainingDate;

    @Schema(description = "Duration of the training in hours", example = "1.5", required = true, minimum = "0", maximum = "100")
    @Range(min = 0, max = 100)
    @NotNull
    private Double trainingDuration;
}
