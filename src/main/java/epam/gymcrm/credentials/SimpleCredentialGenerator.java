package epam.gymcrm.credentials;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SimpleCredentialGenerator implements CredentialGenerator {

    public static final String DELIMITER = ".";
    public final Logger logger = LoggerFactory.getLogger(SimpleCredentialGenerator.class);
    private final EntityManagerFactory entityManagerFactory;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
    private static final int PASSWORD_LENGTH = 10;
    private static final Random RANDOM = new Random();

    @Override
    public String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + DELIMITER + lastName;
        HashSet<String> usernameSet = new HashSet<>();
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            String query = "SELECT u.username FROM User u WHERE u.username LIKE :username";
            usernameSet = new HashSet<>(entityManager.createQuery(query, String.class)
                    .setParameter("username", baseUsername + "%")
                    .getResultList());
        } catch (PersistenceException e) {
            logger.error("Error while generating username", e);
            e.printStackTrace();
        }
        int count = 0;
        String generatedUsername = baseUsername;
        while (usernameSet.contains(generatedUsername)) {
            count++;
            generatedUsername = baseUsername + count;
        }

        return generatedUsername;
    }

    @Override
    public String generatePassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }
}
