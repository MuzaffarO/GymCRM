package epam.gymcrm.dao;

import epam.gymcrm.model.User;

import java.util.Optional;

public interface UserDao extends CRUDDao<User, Integer> {

    Optional<User> findByUsername(String username, String password);

    void changePassword(String username, String newPassword, String oldPassword);

    void updateIsActive(String username, boolean isActive, String password);
}
