package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.TrainingDao;
import epam.gymcrm.model.Training;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDaoImpl extends AbstractCrudImpl<Training, Integer> implements TrainingDao {

    public TrainingDaoImpl() {
        super(Training.class);
    }
}
