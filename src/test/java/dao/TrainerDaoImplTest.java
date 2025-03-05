package dao;

import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {

    @Mock
    private TrainerDao trainerDao;

    private final String TEST_USERNAME = "test_username";
    private final String TEST_PASSWORD = "test_password";

    @Test
    void testGetTrainerTrainingsByUsername() {
        Training training = new Training();
        training.setId(1);

        when(trainerDao.getTrainerTrainingsByUsername(TEST_USERNAME, TEST_PASSWORD))
                .thenReturn(Arrays.asList(training));

        List<Training> result = trainerDao.getTrainerTrainingsByUsername(TEST_USERNAME, TEST_PASSWORD);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }
}
