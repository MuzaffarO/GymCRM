package epam.gymcrm.service;

public interface CRUDServices<D, ID> {

    D add(D dto); // No authentication needed for adding users (like Trainees & Trainers)

    D getById(ID id, String username, String password);

    D update(D dto, String username, String password);

    boolean delete(ID id, String username, String password);
}
