package epam.gymcrm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TrainingRegisterDto {
    @NotBlank
    private String traineeUsername;

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String trainingName;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date trainingDate;

    @NotBlank
    private Double trainingDuration;
}
