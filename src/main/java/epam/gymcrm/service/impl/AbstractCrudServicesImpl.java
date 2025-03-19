package epam.gymcrm.service.impl;

import epam.gymcrm.dao.CRUDDao;
import epam.gymcrm.exceptions.DatabaseException;
import epam.gymcrm.exceptions.UnexpectedException;
import epam.gymcrm.exceptions.UserNotFoundException;
import epam.gymcrm.service.CRUDServices;
import epam.gymcrm.service.mapper.AbstractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public abstract class AbstractCrudServicesImpl<T, D, ID> implements CRUDServices<D, ID> {

    private final CRUDDao<T, ID> crudDao;
    private final AbstractMapper<T, D> mapper;

    @Override
    public D add(D dto) {
        try {
            T entity = mapper.toEntity(dto);
            T savedEntity = crudDao.save(entity)
                    .orElseThrow(() -> new UnexpectedException("Unexpected error occurred while saving data"));
            return mapper.toDto(savedEntity);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public D getById(ID id) {
        try {
            T entity = crudDao.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
            return mapper.toDto(entity);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public D update(D dto) {
        try {
            T entityToUpdate = mapper.toEntity(dto);
            T updatedEntity = crudDao.update(entityToUpdate)
                    .orElseThrow(() -> new UnexpectedException("Unexpected error occurred while updating data"));
            return mapper.toDto(updatedEntity);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public boolean delete(ID id) {
        try {
            return crudDao.delete(id);
        } catch (DataAccessException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}
