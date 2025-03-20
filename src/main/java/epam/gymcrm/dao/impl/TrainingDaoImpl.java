package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class TrainingDaoImpl extends AbstractCrudImpl<Training, Integer> implements TrainingDao {
    @Autowired
    private EntityManagerFactory entityManagerFactory;


    public TrainingDaoImpl() {
        super(Training.class);
    }


    @Override
    public List<Training> findTraineeTrainingsByFilters(String username, Date startDate, Date endDate, String trainerName, String trainingType) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> root = query.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            // Mandatory filter (username)
            Join<Training, Trainee> traineeJoin = root.join("trainee");
            Join<Trainee, User> userJoin = traineeJoin.join("user");
            predicates.add(cb.equal(userJoin.get("username"), username));

            // Optional filters
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), endDate));
            }
            if (trainerName != null && !trainerName.isEmpty()) {
                Join<Training, Trainer> trainerJoin = root.join("trainer");
                Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
                predicates.add(cb.equal(trainerUserJoin.get("firstName"), trainerName));
            }
            if (trainingType != null && !trainingType.isEmpty()) {
                Join<Training, TrainingType> trainingTypeJoin = root.join("trainingType");
                predicates.add(cb.equal(trainingTypeJoin.get("trainingTypeName"), trainingType));
            }

            query.select(root).where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(query).getResultList();
        });
    }



}
