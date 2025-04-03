package epam.gymcrm.dto.training;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.dto.trainee.TraineeDTO;
import epam.gymcrm.dto.trainer.TrainerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDTO {

    private Integer id;
    private TraineeDTO trainee;
    private TrainerDTO trainer;
    private String trainingName;
    private TrainingTypeDTO trainingType;
    private Date trainingDate;
    private Double trainingDuration;
}
