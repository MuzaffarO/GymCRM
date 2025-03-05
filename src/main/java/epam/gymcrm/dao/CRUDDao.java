package epam.gymcrm.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

public interface CRUDDao<T, ID> {

    Optional<T> save(T entity); // No authentication needed for save()

    Optional<T> findById(ID id, String username, String password);

    Optional<T> update(T entity, String username, String password);

    boolean delete(ID id, String username, String password);

    Collection<T> findAll(String username, String password);
}
