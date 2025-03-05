package epam.gymcrm.dao.util;

import jakarta.persistence.EntityManager;

import java.util.function.Consumer;

public interface JpaTransactionVoidFunction extends Consumer<EntityManager> {
    default void beforeTransactionCompletion() {}
    default void afterTransactionCompletion() {}
}
