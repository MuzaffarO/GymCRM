package epam.gymcrm.service.impl;


import epam.gymcrm.dao.TraineeDAO;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.service.TraineeService;
import epam.gymcrm.utils.PasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDAO traineeDAO;

    public TraineeServiceImpl(TraineeDAO traineeDAO) {
        this.traineeDAO = traineeDAO;
    }

    @Override
    public void registerTrainee(Trainee trainee) {
        trainee.setUsername(generateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword("jsut");
        traineeDAO.save(trainee);
        log.info("Trainee registered: {}", trainee.getUsername());
    }


    @Override
    public List<Trainee> getAllTrainees() {
        return traineeDAO.findAll();
    }

    private String generateUsername(String firstName, String lastName) {
        return firstName + "." + lastName;
    }
}

