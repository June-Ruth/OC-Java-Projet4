package com.parkit.parkingsystem.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Configuration for DataBase.
 */
public class DataBaseConfig {

    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    /**
     * Get URL for DB properties (in ressources).
     */
    private static final URL DB_PROPERTIES_URL =
            DataBaseConfig.class.getClassLoader().getResource("db.properties");

    /**
     * Get DataSource for DB configuration.
     * @return Datasource MySQL
     */
    public static DataSource getMysqlDataSource() {
        Properties properties = new Properties();
        FileInputStream fis;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream(new File(DB_PROPERTIES_URL.getFile()));
            properties.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(properties.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(properties.getProperty("MYSQL_DB_USER"));
            mysqlDS.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
        } catch (Exception e) {
            LOGGER.error("Error while getting db properties", e);
        }
        return  mysqlDS;
    }

    /**
     * Manage DB connection.
     * @return DataBase Connection configuration
     */
    public Connection getConnection() {
        LOGGER.info("Create DB connection");
        DataSource ds = getMysqlDataSource();
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Error while getting connection", e);
        }
        return con;
    }

    /**
     * Manage DB connection closing.
     * @param con Connection
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Manage Prepared Statement closing.
     * @param ps PreparedStatement
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Manage ResultSet closing.
     * @param rs ResultSet
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}
