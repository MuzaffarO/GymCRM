package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainerDAO;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.service.TrainerService;
import epam.gymcrm.utils.PasswordGenerator;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;

    @Override
    public void registerTrainer(Trainer trainer) {
        trainer.setUsername(generateUniqueUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(PasswordGenerator.generate());
        trainerDAO.save(trainer);
        log.info("Trainer registered: {}", trainer.getUsername());
    }

    @Override
    public Optional<Trainer> selectTrainer(String username) {
        return trainerDAO.findByUsername(username);
    }

    @Override
    public void updateTrainer(String username, Trainer updatedTrainer) {
        Optional<Trainer> existingTrainer = trainerDAO.findByUsername(username);
        existingTrainer.ifPresentOrElse(
                trainer -> {
                    trainer.setSpecialization(updatedTrainer.getSpecialization());
                    trainerDAO.save(trainer);
                    log.info("Trainer updated: {}", username);
                },
                () -> log.warn("Trainer not found: {}", username)
        );
    }

    @Override
    public List<Trainer> getAllTrainers() {
        return trainerDAO.findAll();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        int count = 0;
        String uniqueUsername = baseUsername;

        while (trainerDAO.findByUsername(uniqueUsername).isPresent()) {
            count++;
            uniqueUsername = baseUsername + count;
        }
        return uniqueUsername;
    }
}
