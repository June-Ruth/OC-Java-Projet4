package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Configuration for DataBase using Singleton pattern.
 */
public enum DataBaseManager {
    /**
     * Instance for Singleton pattern.
     */
    INSTANCE;

    /**
     * @see Logger
     */
    private static final Logger LOGGER =
            LogManager.getLogger(DataBaseManager.class);

    /**
     * @see DataSource
     */
    private DataSource dataSource;

    DataBaseManager() {
        Properties properties = new Properties();
        URL propertiesUrl = DataBaseManager.class.getClassLoader().
                getResource("db.properties");
        File file;
        assert propertiesUrl != null;
        file = new File(propertiesUrl.getFile());
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.load(fis);
            dataSource = DataSourceFactory.get(
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password")
            );
        } catch (IOException e) {
            LogManager.getLogger(DataBaseManager.class)
                    .error("Error while getting db properties", e);
        }
    }

    /**
     * Manage DB connection.
     * @return DataBase Connection configuration
     */
    public Connection getConnection() {
        LOGGER.info("Create DB connection");
        Connection con = null;
        try {
            con = dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Error while getting connection", e);
        }
        return con;
    }
}
