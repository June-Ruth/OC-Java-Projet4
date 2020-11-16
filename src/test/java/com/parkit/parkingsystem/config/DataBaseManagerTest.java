package com.parkit.parkingsystem.config;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DataBaseManagerTest {

    private JdbcDataSource dataSource = new JdbcDataSource();

    @Test
    void getConnectionTestSuccess() throws Exception {
        try (MockedStatic<DataSourceFactory> factoryMock = mockStatic(DataSourceFactory.class)) {
            dataSource.setURL("jdbc:h2:mem:prod;DB_CLOSE_DELAY=-1;INIT=create schema if not exists test\\;");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            factoryMock
                    .when(() -> DataSourceFactory.get(anyString(), anyString(), anyString()))
                    .thenReturn(dataSource);
            Connection con = DataBaseManager.INSTANCE.getConnection();
            assertEquals(dataSource.getConnection().getClientInfo(), con.getClientInfo());
        }
    }
}
