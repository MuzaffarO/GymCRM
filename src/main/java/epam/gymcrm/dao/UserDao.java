package epam.gymcrm.dao;

import epam.gymcrm.model.User;

import java.util.Optional;

public interface UserDao extends CRUDDao<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean changePassword(String username, String newPassword);

    void updateIsActive(String username, boolean isActive, String password);
}
