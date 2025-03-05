package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.model.TrainingType;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDaoImpl extends AbstractCrudImpl<TrainingType, Integer> implements TrainingTypeDao {
    public TrainingTypeDaoImpl() {
        super(TrainingType.class);
    }
}
