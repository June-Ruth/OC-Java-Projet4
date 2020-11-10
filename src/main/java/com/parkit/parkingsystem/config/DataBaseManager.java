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
    INSTANCE;

    /**
     * @see Logger
     */
    private final Logger LOGGER = LogManager.getLogger(DataBaseManager.class);

    private DataSource dataSource;

    DataBaseManager() {
        Properties properties = new Properties();
        FileInputStream fis;
        try {
            URL propertiesUrl = DataBaseManager.class.getClassLoader().getResource("db.properties");
            fis = new FileInputStream(new File(propertiesUrl.getFile()));
            properties.load(fis);
            dataSource = DataSourceFactory.get(
                    properties.getProperty("jdbc.url"),
                    properties.getProperty("jdbc.user"),
                    properties.getProperty("jdbc.password")
            );
        } catch (IOException e) {
            LOGGER.error("Error while getting db properties", e);
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
