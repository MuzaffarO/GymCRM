package epam.gymcrm;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.impl.TrainingTypeServiceImpl;
import epam.gymcrm.mapper.TrainingTypeMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeDTOServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeServices;
    @Mock private MeterRegistry meterRegistry;
    @Mock private Counter mockCounter;

    @Test
    void testGetTrainingType() {
        epam.gymcrm.model.TrainingType type1 = new epam.gymcrm.model.TrainingType();
        type1.setId(1);
        type1.setTrainingTypeName("karate");

        epam.gymcrm.model.TrainingType type2 = new epam.gymcrm.model.TrainingType();
        type2.setId(2);
        type2.setTrainingTypeName("yoga");

        when(trainingTypeRepository.findAll()).thenReturn(List.of(type1, type2));

        List<TrainingTypeDTO> response = trainingTypeServices.getTrainingType();

        assertEquals(2, response.size());
    }

    @Test
    void testCreateTrainingType() {
        lenient().when(meterRegistry.counter(anyString())).thenReturn(mockCounter);
        epam.gymcrm.model.TrainingType newType = new epam.gymcrm.model.TrainingType();
        newType.setTrainingTypeName("boxing");

        epam.gymcrm.model.TrainingType savedType = new epam.gymcrm.model.TrainingType();
        savedType.setId(3);
        savedType.setTrainingTypeName("boxing");

        when(trainingTypeRepository.save(any(epam.gymcrm.model.TrainingType.class))).thenReturn(savedType);

        epam.gymcrm.model.TrainingType response = trainingTypeServices.createTrainingType("boxing");

        assertEquals("boxing", response.getTrainingTypeName());
    }
}