package epam.gymcrm.dao.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

public class TransactionUtil {

    public static <T> T executeInTransaction(EntityManagerFactory factory, JpaTransactionFunction<T> function) {
        T result;
        EntityTransaction txn = null;
        EntityManager entityManager = factory.createEntityManager();

        try {
            txn = entityManager.getTransaction();
            txn.begin();

            function.beforeTransactionCompletion();
            result = function.apply(entityManager);

            txn.commit();
        } catch (Throwable e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return result;
    }

    public static void executeInTransaction(EntityManagerFactory factory, JpaTransactionVoidFunction function) {
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction txn = entityManager.getTransaction();

        try {
            txn.begin();
            function.beforeTransactionCompletion();
            function.accept(entityManager);
            txn.commit();
        } catch (Throwable e) {
            if (txn.isActive()) {
                txn.rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

}
