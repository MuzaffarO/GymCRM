package epam.gymcrm;

import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
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
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceImplTest {

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
        TrainingType type1 = new TrainingType();
        type1.setId(1);
        type1.setTrainingTypeName("karate");

        TrainingType type2 = new TrainingType();
        type2.setId(2);
        type2.setTrainingTypeName("yoga");

        when(trainingTypeRepository.findAll()).thenReturn(List.of(type1, type2));

        List<TrainingTypeDto> response = trainingTypeServices.getTrainingType();

        assertEquals(2, response.size());
    }

    @Test
    void testCreateTrainingType() {
        lenient().when(meterRegistry.counter(anyString())).thenReturn(mockCounter);
        TrainingType newType = new TrainingType();
        newType.setTrainingTypeName("boxing");

        TrainingType savedType = new TrainingType();
        savedType.setId(3);
        savedType.setTrainingTypeName("boxing");

        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(savedType);

        TrainingType response = trainingTypeServices.createTrainingType("boxing");

        assertEquals("boxing", response.getTrainingTypeName());
    }
}