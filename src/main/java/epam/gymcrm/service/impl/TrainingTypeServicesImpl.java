package epam.gymcrm.service.impl;

import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.repository.TrainingTypeRepository;
import epam.gymcrm.service.TrainingTypeServices;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeServicesImpl implements TrainingTypeServices {

    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServicesImpl(TrainingTypeRepository trainingTypeRepository, TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    @Override
    public ResponseEntity<List<TrainingTypeDto>> getTrainingType() {
        List<TrainingTypeDto> trainingTypes = trainingTypeRepository.findAll()
                .stream()
                .map(trainingType -> new TrainingTypeDto(trainingType.getId(), trainingType.getTrainingTypeName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(trainingTypes);
    }

    @Override
    public ResponseEntity<TrainingType> createTrainingType(String name) {
        TrainingType newTrainingType = new TrainingType();
        newTrainingType.setTrainingTypeName(name);

        TrainingType savedTrainingType = trainingTypeRepository.save(newTrainingType);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrainingType);
    }
}
