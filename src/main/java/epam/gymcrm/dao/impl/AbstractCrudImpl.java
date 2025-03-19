package epam.gymcrm.dao.impl;


import jakarta.persistence.EntityManagerFactory;
import epam.gymcrm.dao.CRUDDao;
import epam.gymcrm.dao.util.TransactionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

@Component
public abstract class AbstractCrudImpl<T, ID extends Serializable> implements CRUDDao<T, ID> {

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    private Class<T> entityType;

    public AbstractCrudImpl(Class<T> entityType) {
        this.entityType = entityType;
    }


    @Override
    public Optional<T> save(T entity) {
        return Optional.ofNullable(TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            entityManager.persist(entity);
            return entity;
        }));
    }

    @Override
    public Optional<T> findById(ID id) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            return Optional.ofNullable(entityManager.find(entityType, id));
        });
    }

    @Override
    public Optional<T> update(T entity) {
        return Optional.ofNullable(TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            T mergedEntity = entityManager.merge(entity);
            entityManager.flush();
            return mergedEntity;
        }));
    }


    @Override
    public boolean delete(ID id) {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            T entity = entityManager.find(entityType, id);
            if (entity != null) {
                entityManager.remove(entity);
                return true;
            }
            return false;
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<T> findAll() {
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String query = "from " + entityType.getName();
            return entityManager.createQuery(query, entityType).getResultList();
        });
    }
}
