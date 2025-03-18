package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.TrainingTypeDao;
import epam.gymcrm.dao.util.TransactionUtil;
import epam.gymcrm.model.TrainingType;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeDaoImpl extends AbstractCrudImpl<TrainingType, Integer> implements TrainingTypeDao {
    public TrainingTypeDaoImpl() {
        super(TrainingType.class);
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Override
    public Optional<TrainingType> findByName(String trainingTypeName) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            TypedQuery<TrainingType> query = entityManager.createQuery(
                    "SELECT t FROM TrainingType t WHERE t.trainingTypeName = :name", TrainingType.class);

            query.setParameter("name", trainingTypeName);
            return query.getResultStream().findFirst();
        });
    }
}
