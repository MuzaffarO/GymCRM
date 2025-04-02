package epam.gymcrm.mapper;

import epam.gymcrm.dto.TraineeDto;
import epam.gymcrm.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TraineeMapper implements AbstractMapper<Trainee, TraineeDto> {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "user", expression = "java(userMapper.toEntity(dto.getUser()))")
    public abstract Trainee toEntity(TraineeDto dto);

    @Mapping(target = "user", expression = "java(userMapper.toDto(entity.getUser()))")
    public abstract TraineeDto toDto(Trainee entity);
}
