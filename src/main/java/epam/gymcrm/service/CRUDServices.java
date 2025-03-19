package epam.gymcrm.service;

public interface CRUDServices<D, ID> {

    D add(D dto);

    D getById(ID id);

    D update(D dto);

    boolean delete(ID id);
}
