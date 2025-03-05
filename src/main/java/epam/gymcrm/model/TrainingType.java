package epam.gymcrm.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String trainingTypeName;

    @OneToMany(mappedBy = "trainingType")
    private List<Training> trainings;
}
