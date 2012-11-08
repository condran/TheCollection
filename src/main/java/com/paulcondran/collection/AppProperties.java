package com.paulcondran.collection;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: paul
 * Date: 23/10/12
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class AppProperties {
    protected static final Logger log = Logger.getLogger(AppProperties.class);

    private Properties properties = null;

    private static AppProperties instance;

    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
            instance.properties = instance.loadProperties();
        }
        return instance;
    }

    /**
     * Will read the properties file
     * @return  the loaded properties file
     */
    private Properties loadProperties() {
        Properties prop = new Properties();
        InputStream propertyStream = this.getClass().getResourceAsStream("/AppProperties.properties");
        try {
            prop.load(propertyStream);
        } catch (IOException e) {
            log.error("Could not load the properties files", e);
        }
        return prop;
    }

    /**
     * Will reload the properties file. Ensure to close down database connection and other resources.
     */
    public void reload() {
        properties = loadProperties();
    }

    /**
     * Is the application deployed on heroku
     * @return
     */
    public boolean isHeroku() {
        String heroku = properties.getProperty("heroku", "false");
        return Boolean.parseBoolean(heroku);
    }

    public String getDatabaseConfig() {
        return properties.getProperty("database-config", "database-config");
    }
}
