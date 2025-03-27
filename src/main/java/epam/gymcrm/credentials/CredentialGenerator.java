package epam.gymcrm.credentials;

public interface CredentialGenerator {

    String generateUsername(String firstName, String lastName);

    String generatePassword();
}
