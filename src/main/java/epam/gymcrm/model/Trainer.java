package epam.gymcrm.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne()
    private TrainingType specializationType;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "trainers", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Trainee> trainees;

    @OneToMany(mappedBy = "trainer", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Training> trainingList;

}
