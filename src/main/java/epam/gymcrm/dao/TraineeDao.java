package epam.gymcrm.dao;

import epam.gymcrm.dto.request.TrainerUsernameRequestDto;
import epam.gymcrm.model.Trainee;
import epam.gymcrm.model.Trainer;
import epam.gymcrm.model.Training;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface TraineeDao extends CRUDDao<Trainee, Integer> {

    void deleteByUsername(String username);

    Optional<Trainee> findByUserUsername(String username);

    List<Training> getTraineeTrainingsByUsername(String username);
    List<Trainer> findAllTrainersByUsernameList(List<TrainerUsernameRequestDto> trainers);
    public void updateTraineeAndFlushWithTrainers(String traineeUsername, List<String> trainerUsernames);
}
