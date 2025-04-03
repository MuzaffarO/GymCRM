package epam.gymcrm.dto.trainer;

import epam.gymcrm.dto.trainingtype.TrainingTypeDto;
import epam.gymcrm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {

    private Integer id;
    private TrainingTypeDto specializationType;
    private UserDto user;

}
