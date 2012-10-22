package com.tekinsure.thecollection.data;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This class manages the database connection, saving, updating, and searching entities.
 *
 */
public class CollectionDatabase {
    protected static final Logger log = Logger.getLogger(CollectionDatabase.class);

    private EntityManagerFactory entityManagerFactory = null;
    private EntityManager entityManager = null;

    private static CollectionDatabase instance;

    public static CollectionDatabase getInstance() {
        if (instance == null) {
            instance = new CollectionDatabase();
            instance.connectDatabase();
        }
        return instance;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    /**
     * Will connect to the database if no connection is present
     */
    public void connectDatabase() {

        if (entityManager == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory("collection.odb");
                entityManager = entityManagerFactory.createEntityManager();
            }
            catch (Exception e) {
                log.error("Could not connect to the database.", e);
            }
        }
    }

    public void shutdown() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

}
