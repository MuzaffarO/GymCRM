package epam.gymcrm.service.mapper;

public interface AbstractMapper<E, D> {
    E toEntity(D d);

    D toDto(E e);
}
