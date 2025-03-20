package epam.gymcrm.dao.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TransactionUtil {

    private static final Logger logger = LoggerFactory.getLogger(TransactionUtil.class);

    public static <T> T executeInTransaction(EntityManagerFactory factory, JpaTransactionFunction<T> function) {
        // Retrieve transactionId from MDC or generate a new one
        String transactionId = MDC.get("transactionId");
        if (transactionId == null) {
            transactionId = java.util.UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);
        }

        logger.info("Transaction [{}] STARTED", transactionId);

        T result;
        EntityTransaction txn = null;
        EntityManager entityManager = factory.createEntityManager();

        try {
            txn = entityManager.getTransaction();
            txn.begin();

            function.beforeTransactionCompletion();
            result = function.apply(entityManager);

            entityManager.flush();
            txn.commit();
            logger.info("Transaction [{}] COMMITTED SUCCESSFULLY", transactionId);
        } catch (Throwable e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
                logger.error("Transaction [{}] ROLLED BACK due to error: {}", transactionId, e.getMessage(), e);
            }
            throw e;
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
            MDC.clear(); // Ensure MDC is cleared after transaction completes
        }
        return result;
    }

    public static void executeInTransaction(EntityManagerFactory factory, JpaTransactionVoidFunction function) {
        // Retrieve transactionId from MDC or generate a new one
        String transactionId = MDC.get("transactionId");
        if (transactionId == null) {
            transactionId = java.util.UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);
        }

        logger.info("Transaction [{}] STARTED", transactionId);

        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction txn = entityManager.getTransaction();

        try {
            txn.begin();
            function.beforeTransactionCompletion();
            function.accept(entityManager);
            txn.commit();
            logger.info("Transaction [{}] COMMITTED SUCCESSFULLY", transactionId);
        } catch (Throwable e) {
            if (txn.isActive()) {
                txn.rollback();
                logger.error("Transaction [{}] ROLLED BACK due to error: {}", transactionId, e.getMessage(), e);
            }
            throw e;
        } finally {
            entityManager.close();
            MDC.clear(); // Ensure MDC is cleared after transaction completes
        }
    }
}
























//package epam.gymcrm.dao.util;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.EntityTransaction;
//
//public class TransactionUtil {
//
//    public static <T> T executeInTransaction(EntityManagerFactory factory, JpaTransactionFunction<T> function) {
//        T result;
//        EntityTransaction txn = null;
//        EntityManager entityManager = factory.createEntityManager();
//
//        try {
//            txn = entityManager.getTransaction();
//            txn.begin();
//
//            function.beforeTransactionCompletion();
//            result = function.apply(entityManager);
//
//            entityManager.flush();
//            txn.commit();
//        } catch (Throwable e) {
//            if (txn != null && txn.isActive()) {
//                txn.rollback();
//            }
//            throw e;
//        } finally {
//            if (entityManager.isOpen()) {
//                entityManager.close();
//            }
//        }
//        return result;
//    }
//
//    public static void executeInTransaction(EntityManagerFactory factory, JpaTransactionVoidFunction function) {
//        EntityManager entityManager = factory.createEntityManager();
//        EntityTransaction txn = entityManager.getTransaction();
//
//        try {
//            txn.begin();
//            function.beforeTransactionCompletion();
//            function.accept(entityManager);
//            txn.commit();
//        } catch (Throwable e) {
//            if (txn.isActive()) {
//                txn.rollback();
//            }
//            throw e;
//        } finally {
//            entityManager.close();
//        }
//    }
//
//}
