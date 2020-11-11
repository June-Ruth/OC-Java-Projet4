package com.parkit.parkingsystem.config;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    /**
     * Get DataSource information to allow connect.
     * @param url of properties file
     * @param user user name for DB
     * @param password Password associate for User of DB
     * @return DataSource used
     */
    public static DataSource get(
            final String url, final String user, final String password) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        return dataSource;
    }
}
