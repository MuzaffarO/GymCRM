package epam.gymcrm.service.impl;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.TrainingTypeService;
import epam.gymcrm.mapper.TrainingTypeMapper;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    @Override
    public List<TrainingTypeDTO> getTrainingType() {
        List<TrainingTypeDTO> trainingTypes = trainingTypeRepository.findAll()
                .stream()
                .map(trainingType -> new TrainingTypeDTO(trainingType.getId(), trainingType.getTrainingTypeName()))
                .collect(Collectors.toList());

        return trainingTypes;
    }

    @Override
    public TrainingType createTrainingType(String name) {
        trainingTypeRepository.findByTrainingTypeName(name).ifPresent(t -> {
            throw new BadRequestException("Training type already exists: " + name);
        });

        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setTrainingTypeName(name);
        return trainingTypeRepository.save(newTrainingType);
    }

}
