package epam.gymcrm.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface CRUDDao<T, ID> {

    Optional<T> save(T entity); // No authentication needed for save()

    Optional<T> findById(ID id);

    Optional<T> update(T entity);

    boolean delete(ID id);

    Collection<T> findAll();
}
