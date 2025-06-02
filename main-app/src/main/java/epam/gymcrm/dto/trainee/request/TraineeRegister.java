package epam.gymcrm.dto.trainee.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TraineeRegister {

    @Schema(description = "Trainee's first name", example = "Alice", required = true)
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Schema(description = "Trainee's last name", example = "Smith", required = true)
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Schema(description = "Trainee's date of birth in dd/MM/yyyy format", example = "21/05/2001", required = true, type = "string", format = "date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @Schema(description = "Trainee's address (optional)", example = "123 Main Street, Springfield")
    private String address;
}
