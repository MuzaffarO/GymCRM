package epam.gymcrm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @ManyToMany(mappedBy = "trainers", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JsonBackReference
    @ToString.Exclude
    private List<Trainee> trainees;

    @OneToMany(mappedBy = "trainer", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<Training> trainingList;


}
