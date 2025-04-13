package epam.gymcrm.service.impl;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;
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
        List<TrainingTypeDTO> trainingTypes = trainingTypeRepository.findAll()
                .stream()
                .map(trainingType -> new TrainingTypeDTO(trainingType.getId(), trainingType.getTrainingTypeName()))
                .collect(Collectors.toList());

        return trainingTypes;
    }

    @Override
    public TrainingTypeDTO createTrainingType(String name) {
        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setTrainingTypeName(name);

        trainingTypeRepository.save(newTrainingType);

        return trainingTypeMapper.toDto(newTrainingType);
    }
}
