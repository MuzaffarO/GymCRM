package epam.gymcrm.mapper;

import epam.gymcrm.dto.training.TrainingDTO;
import epam.gymcrm.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TrainingMapper implements AbstractMapper<Training, TrainingDTO> {

    @Autowired
    protected TraineeMapper traineeMapper;
    @Autowired
    protected TrainerMapper trainerMapper;
    @Autowired
    protected TrainingTypeMapper trainingTypeMapper;

    @Mapping(target = "trainee", expression = "java(traineeMapper.toEntity(dto.getTrainee()))")
    @Mapping(target = "trainer", expression = "java(trainerMapper.toEntity(dto.getTrainer()))")
    @Mapping(target = "trainingType", expression = "java(trainingTypeMapper.toEntity(dto.getTrainingType()))")
    public abstract Training toEntity(TrainingDTO dto);

    @Mapping(target = "trainee", expression = "java(traineeMapper.toDto(entity.getTrainee()))")
    @Mapping(target = "trainer", expression = "java(trainerMapper.toDto(entity.getTrainer()))")
    @Mapping(target = "trainingType", expression = "java(trainingTypeMapper.toDto(entity.getTrainingType()))")
    public abstract TrainingDTO toDto(Training entity);
}


