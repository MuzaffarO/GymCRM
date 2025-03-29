package epam.gymcrm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeProfileRequestDto {

    @Schema(description = "Username of the trainee whose profile will be updated", example = "jason.john", required = true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Schema(description = "First name of the trainee", example = "jason", required = true)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Schema(description = "Last name of the trainee", example = "john", required = true)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Schema(description = "Date of birth of the trainee (format: dd/MM/yyyy)", example = "15/08/2000", type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @Schema(description = "Trainee's address (optional)", example = "123 Green Street, Tashkent")
    private String address;

    @Schema(description = "Whether the trainee is active or not", example = "true", required = true)
    @NotNull
    private Boolean isActive;
}
