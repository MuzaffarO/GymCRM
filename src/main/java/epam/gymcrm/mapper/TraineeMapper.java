package epam.gymcrm.mapper;

import epam.gymcrm.dto.trainee.TraineeDTO;
import epam.gymcrm.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TraineeMapper implements AbstractMapper<Trainee, TraineeDTO> {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "user", expression = "java(userMapper.toEntity(dto.getUser()))")
    public abstract Trainee toEntity(TraineeDTO dto);

    @Mapping(target = "user", expression = "java(userMapper.toDto(entity.getUser()))")
    public abstract TraineeDTO toDto(Trainee entity);
}
