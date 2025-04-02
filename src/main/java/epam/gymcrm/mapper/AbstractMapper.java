package epam.gymcrm.mapper;

public interface AbstractMapper<E, D> {
    E toEntity(D d);

    D toDto(E e);
}
