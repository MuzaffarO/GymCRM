package epam.gymcrm.service.mapper;

import epam.gymcrm.dto.TrainerDto;
import epam.gymcrm.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TrainerMapper implements AbstractMapper<Trainer, TrainerDto> {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "user", expression = "java(userMapper.toEntity(dto.getUser()))")
    public abstract Trainer toEntity(TrainerDto dto);

    @Mapping(target = "user", expression = "java(userMapper.toDto(entity.getUser()))")
    public abstract TrainerDto toDto(Trainer entity);
}
