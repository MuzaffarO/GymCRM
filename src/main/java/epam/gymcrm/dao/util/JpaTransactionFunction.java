package epam.gymcrm.dao.util;

import jakarta.persistence.EntityManager;
import org.springframework.cglib.core.internal.Function;

public interface JpaTransactionFunction<T> extends Function<EntityManager, T> {

    default void beforeTransactionCompletion() {
        System.out.println("beforeTransactionCompletion");
    }
    default void afterTransactionCompletion() {
        System.out.println("afterTransactionCompletion");
    }
}
