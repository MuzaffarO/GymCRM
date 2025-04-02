package epam.gymcrm.mapper;

import epam.gymcrm.dto.TrainingDto;
import epam.gymcrm.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TrainingMapper implements AbstractMapper<Training, TrainingDto> {

    @Autowired
    protected TraineeMapper traineeMapper;
    @Autowired
    protected TrainerMapper trainerMapper;
    @Autowired
    protected TrainingTypeMapper trainingTypeMapper;

    @Mapping(target = "trainee", expression = "java(traineeMapper.toEntity(dto.getTrainee()))")
    @Mapping(target = "trainer", expression = "java(trainerMapper.toEntity(dto.getTrainer()))")
    @Mapping(target = "trainingType", expression = "java(trainingTypeMapper.toEntity(dto.getTrainingType()))")
    public abstract Training toEntity(TrainingDto dto);

    @Mapping(target = "trainee", expression = "java(traineeMapper.toDto(entity.getTrainee()))")
    @Mapping(target = "trainer", expression = "java(trainerMapper.toDto(entity.getTrainer()))")
    @Mapping(target = "trainingType", expression = "java(trainingTypeMapper.toDto(entity.getTrainingType()))")
    public abstract TrainingDto toDto(Training entity);
}
