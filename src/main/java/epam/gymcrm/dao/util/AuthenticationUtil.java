package epam.gymcrm.dao.util;

import epam.gymcrm.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtil {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public boolean authenticate(String username, String password) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);

            query.setParameter("username", username);
            query.setParameter("password", password);

            return !query.getResultList().isEmpty();
        } finally {
            entityManager.close();
        }
    }
}
