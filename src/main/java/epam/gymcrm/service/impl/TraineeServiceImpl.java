package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TraineeDAO;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.service.TraineeService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDAO traineeDAO;

    @Override
    public void registerTrainee(Trainee trainee) {
        traineeDAO.save(trainee);
        log.info("Trainee registered: {}", trainee.getUsername());
    }

    @Override
    public Optional<Trainee> selectTrainee(String username) {
        return traineeDAO.findByUsername(username);
    }

    @Override
    public void updateTrainee(String username, Trainee updatedTrainee) {
        Optional<Trainee> existingTrainee = traineeDAO.findByUsername(username);
        existingTrainee.ifPresentOrElse(
                trainee -> {
                    trainee.setDateOfBirth(updatedTrainee.getDateOfBirth());
                    trainee.setAddress(updatedTrainee.getAddress());
                    traineeDAO.save(trainee);
                    log.info("Trainee updated: {}", username);
                },
                () -> log.warn("Trainee not found: {}", username)
        );
    }

    @Override
    public void deleteTrainee(String username) {
        if (traineeDAO.delete(username)) {
            log.info("Trainee deleted: {}", username);
        } else {
            log.warn("Trainee not found: {}", username);
        }
    }

    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAll();
    }
}
