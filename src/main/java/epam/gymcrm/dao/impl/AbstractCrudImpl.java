package epam.gymcrm.dao.impl;

import epam.gymcrm.dao.util.AuthenticationUtil;
import epam.gymcrm.exceptions.InvalidUsernameOrPasswordException;
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

    @Autowired
    private AuthenticationUtil authenticationUtil;

    private Class<T> entityType;

    public AbstractCrudImpl(Class<T> entityType) {
        this.entityType = entityType;
    }

    private void authenticate(String username, String password) {
        if (!authenticationUtil.authenticate(username, password)) {
            throw new InvalidUsernameOrPasswordException("Invalid username or password");
        }
    }

    @Override
    public Optional<T> save(T entity) {
        return Optional.ofNullable(TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            entityManager.persist(entity);
            return entity;
        }));
    }

    @Override
    public Optional<T> findById(ID id, String username, String password) {
        authenticate(username, password);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            return Optional.ofNullable(entityManager.find(entityType, id));
        });
    }

    @Override
    public Optional<T> update(T entity, String username, String password) {
        authenticate(username, password);
        return Optional.ofNullable(TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            return entityManager.merge(entity);
        }));
    }

    @Override
    public boolean delete(ID id, String username, String password) {
        authenticate(username, password);
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
    public Collection<T> findAll(String username, String password) {
        authenticate(username, password);
        return TransactionUtil.executeInTransaction(entityManagerFactory, entityManager -> {
            String query = "from " + entityType.getName();
            return entityManager.createQuery(query, entityType).getResultList();
        });
    }
}
