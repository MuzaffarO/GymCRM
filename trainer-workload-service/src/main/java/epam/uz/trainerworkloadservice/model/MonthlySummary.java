package epam.uz.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "monthly_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_year")
    private Integer year;
    @Column(name = "training_month")
    private Integer month;
    private Double totalHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private TrainerSummary trainer;
}
