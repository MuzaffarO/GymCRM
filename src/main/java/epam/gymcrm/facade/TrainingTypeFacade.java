package epam.gymcrm.facade;

import epam.gymcrm.dto.trainingtype.TrainingTypeDTO;
import epam.gymcrm.model.TrainingType;
import epam.gymcrm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingTypeFacade {

    private final TrainingTypeService trainingTypeService;

    public List<TrainingTypeDTO> getTrainingTypes() {
        return trainingTypeService.getTrainingType();
    }

    public TrainingTypeDTO createTrainingType(String name) {
        return trainingTypeService.createTrainingType(name);
    }
}
