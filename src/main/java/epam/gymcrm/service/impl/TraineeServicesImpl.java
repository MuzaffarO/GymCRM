package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDao;
import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.mapper.TraineeMapper;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServicesImpl extends AbstractCrudServicesImpl<Trainee, TraineeDto, Integer> implements TraineeServices {

    private final TraineeDao traineeDao;
    private final TrainingMapper trainingMapper;

    public TraineeServicesImpl(TraineeDao traineeDao, TraineeMapper mapper, TrainingMapper trainingMapper) {
        super(traineeDao, mapper);
        this.traineeDao = traineeDao;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public void deleteByUsername(String username, String password) {
        try {
            traineeDao.deleteByUsername(username, password);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public List<TrainingDto> getTraineeTrainingsByUsername(String username, String password) {
        try {
            return traineeDao.getTraineeTrainingsByUsername(username, password).stream()
                    .map(trainingMapper::toDto)
                    .toList();
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
