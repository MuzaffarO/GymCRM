package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dto.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.TrainingTypeServices;
import epam.gymcrm.service.mapper.TrainingTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeServicesImpl extends AbstractCrudServicesImpl<TrainingType, TrainingTypeDto, Integer> implements TrainingTypeServices {

    private final TrainingTypeDao trainingTypeDao;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeServicesImpl(TrainingTypeDao trainingTypeDao, TrainingTypeMapper trainingTypeMapper) {
        super(trainingTypeDao, trainingTypeMapper);
        this.trainingTypeDao = trainingTypeDao;
        this.trainingTypeMapper = trainingTypeMapper;
    }
}
