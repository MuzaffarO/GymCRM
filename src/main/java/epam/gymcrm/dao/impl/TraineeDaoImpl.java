package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.util.AuthenticationUtil;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
import jakarta.persistence.EntityManagerFactory;
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
    private AuthenticationUtil authenticationUtil;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public TraineeDaoImpl() {
        super(Trainee.class);
    }

    private void authenticate(String username, String password) {
        if (!authenticationUtil.authenticate(username, password)) {
            throw new InvalidUsernameOrPasswordException("Invalid username or password");
        }
    }

    @Override
    public void deleteByUsername(String username, String password) {
        authenticate(username, password);
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            Query query = entityManager.createQuery(
                    "DELETE FROM Trainee t WHERE t.user.username = :username");

            query.setParameter("username", username);
            query.executeUpdate();
        });
    }

    @Override
    public Optional<Trainee> findByUserUsername(String username, String password) {
        authenticate(username, password);
        log.info("Find by username {}", username);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t where t.user.username = :username", Trainee.class);

            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        });
    }

    @Override
    public List<Training> getTraineeTrainingsByUsername(String username, String password) {
        authenticate(username, password);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String queryString = "SELECT t FROM Training t JOIN t.trainee usr WHERE usr.user.username = :username";

            TypedQuery<Training> query = entityManager.createQuery(queryString, Training.class);
            query.setParameter("username", username);

            return query.getResultList();
        });
    }
}
