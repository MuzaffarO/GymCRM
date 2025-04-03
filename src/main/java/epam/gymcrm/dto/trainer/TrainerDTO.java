package epam.gymcrm.dto.trainer;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDTO {

    private Integer id;
    private TrainingTypeDTO specializationType;
    private UserDTO user;

}
