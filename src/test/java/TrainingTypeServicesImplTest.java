import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.impl.TrainingTypeServicesImpl;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServicesImplTest {

    @Mock
    private TrainingTypeDao trainingTypeDao;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    private TrainingTypeServicesImpl trainingTypeServices;

    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainingType = new TrainingType();
        trainingType.setId(1);
        trainingType.setTrainingTypeName("Yoga");
    }

    @Test
    void testGetTrainingType_Success() {
        // Arrange
        TrainingTypeDto expectedDto = new TrainingTypeDto(trainingType.getId(), trainingType.getTrainingTypeName());
        when(trainingTypeDao.findAll()).thenReturn(List.of(trainingType));

        // Act
        ResponseEntity<List<TrainingTypeDto>> response = trainingTypeServices.getTrainingType();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(expectedDto, response.getBody().get(0));

        verify(trainingTypeDao, times(1)).findAll();
    }

    @Test
    void testGetTrainingType_EmptyList() {
        // Arrange
        when(trainingTypeDao.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<TrainingTypeDto>> response = trainingTypeServices.getTrainingType();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(trainingTypeDao, times(1)).findAll();
    }

    @Test
    void testGetTrainingType_DatabaseException() {
        // Arrange
        when(trainingTypeDao.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            trainingTypeServices.getTrainingType();
        });
        assertEquals("Database error", exception.getMessage());

        verify(trainingTypeDao, times(1)).findAll();
    }
}
