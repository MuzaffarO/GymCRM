package epam.gymcrm.repository.specifications;

import epam.gymcrm.model.Training;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TrainerSpecification {

    public static Specification<Training> findTrainerTrainingsByFilters(String username, Date startDate, Date endDate, String traineeName) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();  // Default is an "AND" conjunction

            // Mandatory filter (trainer's username)
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(trainerUserJoin.get("username"), username));

            // Optional filters
            if (startDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("trainingDate"), startDate));
            }
            if (endDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("trainingDate"), endDate));
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                Join<Training, Trainee> traineeJoin = root.join("trainee");
                Join<Trainee, User> traineeUserJoin = traineeJoin.join("user");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(traineeUserJoin.get("firstName"), traineeName));
            }

            return predicate;
        };
    }
}
