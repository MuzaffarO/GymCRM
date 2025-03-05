package epam.gymcrm.service.impl;

import epam.gymcrm.dao.UserDao;
import epam.gymcrm.dto.UserDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.User;
import epam.gymcrm.service.UsersServices;
import epam.gymcrm.service.mapper.UserMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServicesImpl extends AbstractCrudServicesImpl<User, UserDto, Integer> implements UsersServices {

    private final UserDao userDao;
    private final UserMapper userMapper;

    public UsersServicesImpl(UserDao userDao, UserMapper userMapper) {
        super(userDao, userMapper);
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByUsername(String username, String password) {
        try {
            return userDao.findByUsername(username, password);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public void changePassword(String username, String newPassword, String currentPassword) {
        try {
            userDao.changePassword(username, newPassword, currentPassword);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
