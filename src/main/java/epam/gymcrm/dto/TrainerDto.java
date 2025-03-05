package epam.gymcrm.dto;

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
