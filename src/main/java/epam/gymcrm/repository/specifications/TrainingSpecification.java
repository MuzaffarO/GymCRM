package epam.gymcrm.repository.specifications;

import epam.gymcrm.model.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class TrainingSpecification {

    public static Specification<Training> findTraineeTrainingsByFilters(String username, Date startDate, Date endDate, String trainerName, String trainingType) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();  // Default is an "AND" conjunction

            // Mandatory filter (username)
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> userJoin = traineeJoin.join("user");
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(userJoin.get("username"), username));

            // Optional filters
            if (startDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("trainingDate"), startDate));
            }
            if (endDate != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("trainingDate"), endDate));
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                Join<Training, Trainer> trainerJoin = root.join("trainer");
                Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(trainerUserJoin.get("firstName"), trainerName));
            }
            if (trainingType != null && !trainingType.isEmpty()) {
                Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(trainingTypeJoin.get("trainingTypeName"), trainingType));
            }

            return predicate;
        };
    }
}
