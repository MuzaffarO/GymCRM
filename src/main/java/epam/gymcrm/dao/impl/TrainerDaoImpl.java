package epam.gymcrm.dao.impl;

import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl extends AbstractCrudImpl<Trainer, Integer> implements TrainerDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public TrainerDaoImpl() {
        super(Trainer.class);
    }


    @Override
    public List<Training> getTrainerTrainingsByUsername(String username) {
        log.info("Get trainer trainings by username: {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String queryString = "SELECT t FROM Training t JOIN t.trainer usr WHERE usr.user.username = :username";

            TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
            query.setParameter("username", username);

            return query.getResultList();
        });
    }

    @Override
    public Optional<Trainer> findByUserUsername(String username) {
        log.info("Find by username: {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            try {
                TypedQuery<Trainer> query = entityManager.createQuery(
                        "SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class);
                query.setParameter("username", username);
                return Optional.ofNullable(query.getSingleResult());
            } catch (NoResultException ex) {
                log.warn("No Trainer found for username: {}", username);
                return Optional.empty();
            }
        });
    }


    @Override
    public List<Trainer> findNotAssignedActiveTrainers(String username) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            return entityManager.createQuery(
                            "SELECT t FROM Trainer t " +
                                    "WHERE t.user.isActive = true " +
                                    "AND t NOT IN (SELECT tt FROM Trainee tr JOIN tr.trainers tt WHERE tr.user.username = :username)",
                            Trainer.class)
                    .setParameter("username", username)
                    .getResultList();
        });
    }

    @Override
    public List<Training> findTrainerTrainingsByFilters(String username, Date startDate, Date endDate, String traineeName) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Training> query = cb.createQuery(Training.class);
            Root<Training> root = query.from(Training.class);

            List<Predicate> predicates = new ArrayList<>();

            // Mandatory filter (trainer's username)
            Join<Training, Trainer> trainerJoin = root.join("trainer");
            Join<Trainer, User> trainerUserJoin = trainerJoin.join("user");
            predicates.add(cb.equal(trainerUserJoin.get("username"), username));

            // Optional filters
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDate"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("trainingDate"), endDate));
            }
            if (traineeName != null && !traineeName.isEmpty()) {
                Join<Training, Trainee> traineeJoin = root.join("trainee");
                Join<Trainee, User> traineeUserJoin = traineeJoin.join("user");
                predicates.add(cb.equal(traineeUserJoin.get("firstName"), traineeName));
            }

            query.select(root).where(predicates.toArray(new Predicate[0]));

            return entityManager.createQuery(query).getResultList();
        });
    }


}
