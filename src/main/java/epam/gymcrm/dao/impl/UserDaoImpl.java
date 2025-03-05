package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.util.AuthenticationUtil;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
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

    @Autowired
    private AuthenticationUtil authenticationUtil;

    public UserDaoImpl() {
        super(User.class);
    }

    private void authenticate(String username, String password) {
        if (!authenticationUtil.authenticate(username, password)) {
            throw new InvalidUsernameOrPasswordException("Invalid username or password");
        }
    }

    @Override
    public Optional<User> findByUsername(String username, String password) {
        authenticate(username, password);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username", User.class);

            query.setParameter("username", username);

            try {
                return Optional.ofNullable(query.getSingleResult());
            } catch (NoResultException ex) {
                throw new UserNotFoundException("User with username " + username + " not found");
            }
        });
    }

    @Override
    public void changePassword(String username, String newPassword, String oldPassword) {
        authenticate(username, oldPassword);
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<User> query = entityManager.createQuery(
                    "UPDATE User u SET u.password = :newPassword WHERE u.username = :username", User.class);

            query.setParameter("username", username);
            query.setParameter("newPassword", newPassword);

            query.executeUpdate();
        });
    }

    @Override
    public void updateIsActive(String username, boolean isActive, String password) {
        authenticate(username, password);
        TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<User> query = entityManager.createQuery(
                    "UPDATE User u SET u.isActive = :isActive WHERE u.username = :username", User.class);

            query.setParameter("username", username);
            query.setParameter("isActive", isActive);

            query.executeUpdate();
        });
    }
}