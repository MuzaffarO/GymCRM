package epam.gymcrm.dao.impl;

import epam.gymcrm.model.Trainer;
import jakarta.persistence.*;
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

    public void updateTraineeAndFlushWithTrainers(String traineeUsername, List<String> trainerUsernames) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            Trainee trainee = em.createQuery("SELECT t FROM Trainee t JOIN FETCH t.trainers WHERE t.user.username = :username", Trainee.class)
                    .setParameter("username", traineeUsername)
                    .getSingleResult();

            List<Trainer> newTrainers = em.createQuery("SELECT t FROM Trainer t WHERE t.user.username IN :usernames", Trainer.class)
                    .setParameter("usernames", trainerUsernames)
                    .getResultList();

            trainee.setTrainers(newTrainers);

            em.flush();

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Failed to update and flush trainee", e);
        } finally {
            em.close();
        }
    }

}

