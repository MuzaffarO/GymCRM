package epam.gymcrm.security;

import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;

public interface AuthService {

    boolean authenticate(String username, String password) throws InvalidUsernameOrPasswordException;

}
