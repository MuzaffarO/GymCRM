package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainingDAO;
import epam.gymcrm.model.Training;
import epam.gymcrm.service.TrainingService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@NoArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDAO trainingDAO;

    @Override
    public void createTraining(Training training) {
        trainingDAO.save(training);
        log.info("Training created: {}", training.getTrainingName());
    }

    @Override
    public List<Training> selectTraining(String trainingName) {
        List<Training> trainings = trainingDAO.findByTrainingName(trainingName);
        if (trainings.isEmpty()) {
            log.warn("No trainings found for: {}", trainingName);
        } else {
            log.info("Found {} training(s) for: {}", trainings.size(), trainingName);
        }
        return trainings;
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingDAO.findAll();
    }
}


