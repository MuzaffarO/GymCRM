package epam.uz.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trainer_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainerSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;
}
