package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.util.AuthenticationUtil;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl extends AbstractCrudImpl<Trainer, Integer> implements TrainerDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AuthenticationUtil authenticationUtil;

    public TrainerDaoImpl() {
        super(Trainer.class);
    }

    private void authenticate(String username, String password) {
        if (!authenticationUtil.authenticate(username, password)) {
            throw new InvalidUsernameOrPasswordException("Invalid username or password");
        }
    }

    @Override
    public List<Training> getTrainerTrainingsByUsername(String username, String password) {
        authenticate(username, password);
        log.info("Get trainer trainings by username: {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String queryString = "SELECT t FROM Training t JOIN t.trainer usr WHERE usr.user.username = :username";

            TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
            query.setParameter("username", username);

            return query.getResultList();
        });
    }

    @Override
    public Optional<Trainer> findByUserUsername(String username, String password) {
        authenticationUtil.authenticate(username, password);
        log.info("Find by username: {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<Trainer> query = entityManager.createQuery(
                    "SELECT t FROM Trainer t where t.user.username = :username", Trainer.class);

            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        });
    }

    @Override
    public List<Trainer> findNotAssignedTrainers(String username, String password) {
        authenticate(username, password);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            return entityManager.createQuery("SELECT t from Trainer t where t.user.isActive = true AND t.trainees IS EMPTY", Trainer.class).getResultList();
        });
    }
}
