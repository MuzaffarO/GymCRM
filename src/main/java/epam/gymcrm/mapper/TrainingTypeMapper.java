package epam.gymcrm.mapper;

import epam.gymcrm.dto.trainingtype.TrainingTypeDto;
import epam.gymcrm.model.TrainingType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TrainingTypeMapper implements AbstractMapper<TrainingType, TrainingTypeDto> {
    public abstract TrainingType toEntity(TrainingTypeDto dto);

//    @Mapping(target = "trainings", ignore = true)
    public abstract TrainingTypeDto toDto(TrainingType entity);
}
