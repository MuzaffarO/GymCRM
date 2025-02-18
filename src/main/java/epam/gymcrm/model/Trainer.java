package epam.gymcrm.model;



import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trainer extends User {
    private String specialization;
}
