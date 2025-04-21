package epam.gymcrm.service;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.impl.TrainingTypeServiceImpl;
import epam.gymcrm.mapper.TrainingTypeMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingTypeServiceImplTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainingTypeMapper trainingTypeMapper;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeServices;
//    @Mock private MeterRegistry meterRegistry;
//    @Mock private Counter mockCounter;

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
        String trainingName = "boxing";
        TrainingType newType = new TrainingType();
        newType.setTrainingTypeName(trainingName);

        TrainingTypeDTO mappedDto = new TrainingTypeDTO();
        mappedDto.setTrainingTypeName(trainingName);

        when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(newType);
        when(trainingTypeMapper.toDto(any(TrainingType.class))).thenReturn(mappedDto);

        TrainingType response = trainingTypeServices.createTrainingType(trainingName);

        assertNotNull(response);
        assertEquals("boxing", response.getTrainingTypeName());
    }

}