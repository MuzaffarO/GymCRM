package dao;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDaoImplTest {

    @Mock
    private TraineeDao traineeDao;

    private final String TEST_USERNAME = "test_username";
    private final String TEST_PASSWORD = "test_password";

    @Test
    void testDeleteByUsername() {
        doNothing().when(traineeDao).deleteByUsername(TEST_USERNAME, TEST_PASSWORD);

        traineeDao.deleteByUsername(TEST_USERNAME, TEST_PASSWORD);

        verify(traineeDao, times(1)).deleteByUsername(TEST_USERNAME, TEST_PASSWORD);
    }

    @Test
    void testGetTraineeTrainingsByUsername() {
        Training training = new Training();
        training.setId(1);

        when(traineeDao.getTraineeTrainingsByUsername(TEST_USERNAME, TEST_PASSWORD))
                .thenReturn(Arrays.asList(training));

        List<Training> result = traineeDao.getTraineeTrainingsByUsername(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    public void findByUserUsername() {
        String username = "abc";
        Optional<Trainee> expected = Optional.empty();
        Optional<Trainee> actual = traineeDao.findByUserUsername(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(expected, actual);
    }
}
