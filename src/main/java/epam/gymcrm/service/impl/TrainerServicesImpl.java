package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainerDao;
import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.service.TrainerServices;
import epam.gymcrm.service.mapper.TrainerMapper;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServicesImpl extends AbstractCrudServicesImpl<Trainer, TrainerDto, Integer> implements TrainerServices {

    private final TrainerDao trainerDao;
    private final TrainingMapper trainingMapper;

    public TrainerServicesImpl(TrainerDao trainerDao, TrainerMapper mapper, TrainingMapper trainingMapper) {
        super(trainerDao, mapper);
        this.trainerDao = trainerDao;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public List<TrainingDto> getTrainerTrainingsByUsername(String username) {
        try {
            return trainerDao.getTrainerTrainingsByUsername(username).stream()
                    .map(trainingMapper::toDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
