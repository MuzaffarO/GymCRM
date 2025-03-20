package epam.gymcrm.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import epam.gymcrm.dto.response.SpecializationNameDto;
import epam.gymcrm.model.TrainingType;
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
public class TraineeTrainingsRequestDto {
    @NotBlank
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date periodTo;
    private String trainerName;
    private String trainingType;
}
