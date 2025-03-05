package epam.gymcrm.dao.datasource;

public interface CredentialGenerator {

    String generateUsername(String firstName, String lastName);

    String generatePassword();
}
