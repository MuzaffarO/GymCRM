package epam.gymcrm.mapper;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TrainingTypeMapper implements AbstractMapper<TrainingType, TrainingTypeDTO> {
    public abstract TrainingType toEntity(TrainingTypeDTO dto);

//    @Mapping(target = "trainings", ignore = true)
    public abstract TrainingTypeDTO toDto(TrainingType entity);
}
