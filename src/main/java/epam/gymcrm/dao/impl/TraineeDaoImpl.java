package epam.gymcrm.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TraineeDaoImpl extends AbstractCrudImpl<Trainee, Integer> implements TraineeDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public TraineeDaoImpl() {
        super(Trainee.class);
    }


//    @Override
//    public void deleteByUsername(String username) {
//        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
//            Query query = entityManager.createQuery(
//                    "DELETE FROM Trainee t WHERE t.user.username = :username");
//
//            query.setParameter("username", username);
//            query.executeUpdate();
//        });
//    }

    @Override
    public void deleteByUsername(String username) {
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            entityManager.createQuery("DELETE FROM Trainee t WHERE t.user IN (SELECT u FROM User u WHERE u.username = :username)")
                    .setParameter("username", username)
                    .executeUpdate();

            entityManager.createQuery("DELETE FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .executeUpdate();
        });
    }


    @Override
    public Optional<Trainee> findByUserUsername(String username) {
        log.info("Finding Trainee by username: {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            try {
                return Optional.ofNullable(
                        entityManager.createQuery(
                                        "SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
                                .setParameter("username", username)
                                .setMaxResults(1)
                                .getSingleResult()
                );
            } catch (NoResultException e) {
                log.warn("No Trainee found for username: {}", username);
                return Optional.empty();
            }
        });
    }


    @Override
    public List<Training> getTraineeTrainingsByUsername(String username) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String queryString = "SELECT t FROM Training t JOIN t.trainee usr WHERE usr.user.username = :username";

            TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
            query.setParameter("username", username);

            return query.getResultList();
        });
    }
}
