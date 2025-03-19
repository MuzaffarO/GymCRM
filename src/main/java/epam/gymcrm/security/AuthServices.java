package epam.gymcrm.security;

import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;

public interface AuthServices {

    void authenticate(String username, String password) throws InvalidUsernameOrPasswordException;

}
