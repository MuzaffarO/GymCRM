package epam.gymcrm.service.impl;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.TrainingTypeService;
import epam.gymcrm.mapper.TrainingTypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServiceImpl(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public List<TrainingTypeDTO> getTrainingType() {
        List<TrainingTypeDTO> trainingTypeDTOS = trainingTypeRepository.findAll()
                .stream()
                .map(trainingType -> new TrainingTypeDTO(trainingType.getId(), trainingType.getTrainingTypeName()))
                .collect(Collectors.toList());

        return trainingTypeDTOS;
    }

    @Override
    public epam.gymcrm.model.TrainingType createTrainingType(String name) {
        epam.gymcrm.model.TrainingType newTrainingType = new epam.gymcrm.model.TrainingType();
        newTrainingType.setTrainingTypeName(name);

        trainingTypeRepository.save(newTrainingType);

        return newTrainingType;
    }
}
