package epam.gymcrm.dto.trainee;

import epam.gymcrm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeDto {

    private Integer id;
    private Date dateOfBirth;
    private String address;
    private UserDto user;

}
