package epam.gymcrm.mapper;

import epam.gymcrm.dto.trainer.TrainerDTO;
import epam.gymcrm.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TrainerMapper implements AbstractMapper<Trainer, TrainerDTO> {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "user", expression = "java(userMapper.toEntity(dto.getUser()))")
    public abstract Trainer toEntity(TrainerDTO dto);

    @Mapping(target = "user", expression = "java(userMapper.toDto(entity.getUser()))")
    public abstract TrainerDTO toDto(Trainer entity);
}
