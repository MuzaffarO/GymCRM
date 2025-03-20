package epam.gymcrm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDto {

    private Integer id;
    private TraineeDto trainee;
    private TrainerDto trainer;
    private String trainingName;
    private TrainingTypeDto trainingType;
    private Date trainingDate;
    private Double trainingDuration;
}
