package dao;

import epam.gymcrm.dao.UserDao;
import epam.gymcrm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoImplTest {

    private static final String USERNAME = "John";
    private static final String PASSWORD = "password123";
    private static final String NEW_PASSWORD = "Test1234";

    @Mock
    UserDao userDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenUsernameAndPassword_whenFindByUsername_thenSuccess() {
        User user = new User();
        user.setUsername(USERNAME);
        when(userDao.findByUsername(USERNAME, PASSWORD)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userDao.findByUsername(USERNAME, PASSWORD);

        assertTrue(foundUser.isPresent());
        verify(userDao).findByUsername(USERNAME, PASSWORD);
    }

    @Test
    public void givenUsernameAndPassword_whenFindByUsername_thenNotFound() {
        when(userDao.findByUsername(USERNAME, PASSWORD)).thenReturn(Optional.empty());

        Optional<User> foundUser = userDao.findByUsername(USERNAME, PASSWORD);

        assertFalse(foundUser.isPresent());
        verify(userDao).findByUsername(USERNAME, PASSWORD);
    }

    @Test
    public void givenUsernameAndPassword_whenChangePassword_thenSuccess() {
        userDao.changePassword(USERNAME, NEW_PASSWORD, PASSWORD);

        verify(userDao).changePassword(USERNAME, NEW_PASSWORD, PASSWORD);
    }

    @Test
    public void updateIsActive() {
        userDao.updateIsActive(USERNAME, true, PASSWORD);

        verify(userDao, times(1)).updateIsActive(USERNAME, true, PASSWORD);
    }

    @Test
    public void save() {
        User entity = new User();
        when(userDao.save(entity)).thenReturn(Optional.of(entity));

        Optional<User> actual = userDao.save(entity);

        assertTrue(actual.isPresent());
        assertEquals(entity, actual.get());
    }

    @Test
    public void findById() {
        Integer id = 1;
        User user = new User();
        user.setId(id);
        when(userDao.findById(id, USERNAME, PASSWORD)).thenReturn(Optional.of(user));

        Optional<User> actual = userDao.findById(id, USERNAME, PASSWORD);

        assertTrue(actual.isPresent());
        assertEquals(id, actual.get().getId());
    }

    @Test
    public void update() {
        User entity = new User();
        when(userDao.update(entity, USERNAME, PASSWORD)).thenReturn(Optional.of(entity));

        Optional<User> actual = userDao.update(entity, USERNAME, PASSWORD);

        assertTrue(actual.isPresent());
        assertEquals(entity, actual.get());
    }

    @Test
    public void findAll() {
        when(userDao.findAll(USERNAME, PASSWORD)).thenReturn(Collections.emptyList());

        Collection<User> actual = userDao.findAll(USERNAME, PASSWORD);

        assertEquals(Collections.emptyList(), actual);
    }
}
