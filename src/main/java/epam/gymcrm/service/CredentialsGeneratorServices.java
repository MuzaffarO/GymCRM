package epam.gymcrm.service;

import java.util.function.Predicate;

public interface CredentialsGeneratorServices {

    String generateUsername(String trainerFirstName, String traineeLastName, Predicate<String> isUsernameExists);

    String generatePassword();
}
