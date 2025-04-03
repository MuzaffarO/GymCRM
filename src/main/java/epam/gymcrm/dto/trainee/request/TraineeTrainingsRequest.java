package epam.gymcrm.dto.trainee.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsRequest {

    @Schema(description = "Username of the trainee whose training list is being requested", example = "john.doe", required = true)
    @NotBlank
    private String username;

    @Schema(description = "Start date for filtering trainings (format: dd/MM/yyyy)", example = "01/03/2025", type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodFrom;

    @Schema(description = "End date for filtering trainings (format: dd/MM/yyyy)", example = "31/03/2025", type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodTo;

    @Schema(description = "Filter trainings by trainer name (optional)", example = "john")
    private String trainerName;

    @Schema(description = "Filter trainings by training type name (optional)", example = "soccer")
    private String trainingType;
}
