package epam.gymcrm.service.impl;

import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.dto.request.TraineeTrainingsRequestDto;
import epam.gymcrm.dto.response.TraineeTrainingsListResponseDto;
import epam.gymcrm.model.Training;
import epam.gymcrm.service.TraineeServices;
import epam.gymcrm.service.TrainingServices;
import epam.gymcrm.service.mapper.TrainingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TrainingServicesImpl extends AbstractCrudServicesImpl<Training, TrainingDto, Integer> implements TrainingServices {

    private final TrainingDao trainingDao;
    private final TraineeServices traineeServices;
    private final TrainingMapper trainingMapper;

    public TrainingServicesImpl(TrainingDao trainingDao, TraineeServices traineeServices, TrainingMapper mapper) {
        super(trainingDao, mapper);
        this.trainingDao = trainingDao;
        this.traineeServices = traineeServices;
        this.trainingMapper = mapper;
    }

    @Override
    public ResponseEntity<List<TraineeTrainingsListResponseDto>> getTrainingsList(TraineeTrainingsRequestDto trainingsRequestDto) {
        traineeServices.getByUsername(trainingsRequestDto.getUsername());

        List<Training> trainings = trainingDao.findTraineeTrainingsByFilters(
                trainingsRequestDto.getUsername(),
                trainingsRequestDto.getPeriodFrom(),
                trainingsRequestDto.getPeriodTo(),
                trainingsRequestDto.getTrainerName(),
                trainingsRequestDto.getTrainingType()
        );

        return ResponseEntity.ok(trainings.stream()
                .map(training -> new TraineeTrainingsListResponseDto(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType().getTrainingTypeName(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName()
                ))
                .toList());
    }

}
