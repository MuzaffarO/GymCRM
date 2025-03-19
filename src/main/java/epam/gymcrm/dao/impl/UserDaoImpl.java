package epam.gymcrm.dao.impl;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import epam.gymcrm.dao.UserDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Log4j2
public class UserDaoImpl extends AbstractCrudImpl<User, Integer> implements UserDao {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public UserDaoImpl() {
        super(User.class);
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            try {
                return Optional.ofNullable(query.getSingleResult());
            } catch (NoResultException ex) {
                return Optional.empty();
            }
        });
    }


    @Override
    public void changePassword(String username, String newPassword) {
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            entityManager.createQuery(
                            "UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
                    .setParameter("username", username)
                    .setParameter("newPassword", newPassword)
                    .executeUpdate();
        });
    }

    @Override
    public void updateIsActive(String username, boolean isActive, String password) {
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<User> query = entityManager.createQuery(
                    "UPDATE User u SET u.isActive = :isActive WHERE u.username = :username", User.class);

            query.setParameter("username", username);
            query.setParameter("isActive", isActive);

            query.executeUpdate();
        });
    }
}