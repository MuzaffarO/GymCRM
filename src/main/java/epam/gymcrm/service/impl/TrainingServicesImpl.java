package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.model.Training;
import epam.gymcrm.service.TrainingServices;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainingServicesImpl extends AbstractCrudServicesImpl<Training, TrainingDto, Integer> implements TrainingServices {

    private final TrainingDao trainingDao;
    private final TrainingMapper trainingMapper;

    public TrainingServicesImpl(TrainingDao trainingDao, TrainingMapper mapper) {
        super(trainingDao, mapper);
        this.trainingDao = trainingDao;
        this.trainingMapper = mapper;
    }
}
