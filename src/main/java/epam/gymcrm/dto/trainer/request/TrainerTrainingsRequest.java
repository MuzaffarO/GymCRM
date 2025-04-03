package epam.gymcrm.dto.trainer.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainingsRequest {

    @Schema(description = "Username of the trainer", example = "john.doe", required = true)
    @NotBlank
    private String username;

    @Schema(description = "Start date of the training search period (optional)", example = "01/03/2025", type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodFrom;

    @Schema(description = "End date of the training search period (optional)", example = "31/03/2025", type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodTo;

    @Schema(description = "Optional filter by trainee's name", example = "Alice")
    private String traineeName;
}
