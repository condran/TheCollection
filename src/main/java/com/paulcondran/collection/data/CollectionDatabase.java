package com.paulcondran.collection.data;

import com.paulcondran.collection.AppProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.net.URI;
import java.util.HashMap;

/**
 * This class manages the database connection, saving, updating, and searching entities.
 * A very simple singleton implementation for ease of prototyping.
 *
 */
public class CollectionDatabase {
    protected static final Logger log = Logger.getLogger(CollectionDatabase.class);

    private EntityManagerFactory entityManagerFactory = null;
    private EntityManager entityManager = null;
    private String currentlyConnectedUnitName = null;

    private static CollectionDatabase instance;

    public static CollectionDatabase getInstance() {
        if (instance == null) {
            instance = new CollectionDatabase();
        }
        if (instance.getEntityManagerFactory() == null || instance.getEntityManager() == null) {
            instance.connectDatabase();
        }
        return instance;
    }

    private CollectionDatabase() {

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
    public void connectDatabase(String persistenceUnitName) {
        // If the request is to connect to a different database then, 
        // shutdown current one and start again
        if (persistenceUnitName != null &&
                !persistenceUnitName.equals(currentlyConnectedUnitName))
        {
            shutdown();
        }

        if (entityManager == null) {
            try {
                if (entityManagerFactory == null) {
                    String persistenceUnit = persistenceUnitName == null ? AppProperties.getInstance().getDatabaseConfig() : persistenceUnitName;
                    // Check for environment URL from Heroku
                    String databaseURL = System.getenv("DATABASE_URL");
                    if (StringUtils.isNotBlank(databaseURL)) {
                        URI dbUri = new URI(databaseURL);

                        final String username = dbUri.getUserInfo().split(":")[0];
                        final String password = dbUri.getUserInfo().split(":")[1];
                        final String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
                        log.debug("DATABASE_URL = " + dbUri);
                        log.debug("username = " + username);
                        log.debug("password = " + password);
                        log.debug("DB_URL = " + dbUrl);

                        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, new HashMap<String,String>()
                        {{
                                put("javax.persistence.jdbc.user", username);
                                put("javax.persistence.jdbc.password", password);
                                put("javax.persistence.jdbc.url", dbUrl);
                        }}
                        );

                    } else {
                        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
                        log.debug("DATABASE_URL is empty, using " + entityManagerFactory.getProperties().get("javax.persistence.jdbc.url"));
                    }
                }
                currentlyConnectedUnitName = persistenceUnitName;

                entityManager = entityManagerFactory.createEntityManager();

            }
            catch (Exception e) {
                log.error("Could not connect to the database.", e);
                e.printStackTrace();
            }
        }
    }

    public void connectDatabase() {
        connectDatabase(null);
    }

    /**
     * Will save an object to the database
     *
     * @param object
     */
    public void persist(Object object) {
        connectDatabase();
        EntityTransaction transaction = getEntityManager().getTransaction();
        if (transaction.isActive())
        {
            transaction.rollback();
        }
        
        transaction.begin();

        getEntityManager().persist(object);
        getEntityManager().flush();

        transaction.commit();
    }

    /**
     * Will save an object to the database
     *
     * @param object
     */
    public void remove(Object object) {
        connectDatabase();
        EntityTransaction transaction = getEntityManager().getTransaction();
        if (transaction.isActive())
        {
            transaction.rollback();
        }
        
        transaction.begin();

        getEntityManager().remove(object);
        getEntityManager().flush();

        transaction.commit();
    }

    public void shutdown() {
        if (entityManager != null) {
            entityManager.close();
            entityManager = null;
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }


}
