package epam.gymcrm.mapper;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class TrainingTypeMapper implements AbstractMapper<epam.gymcrm.model.TrainingType, TrainingTypeDTO> {
    public abstract epam.gymcrm.model.TrainingType toEntity(TrainingTypeDTO dto);

//    @Mapping(target = "trainings", ignore = true)
    public abstract TrainingTypeDTO toDto(epam.gymcrm.model.TrainingType entity);
}
