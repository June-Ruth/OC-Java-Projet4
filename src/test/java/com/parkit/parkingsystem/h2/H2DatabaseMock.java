package com.parkit.parkingsystem.h2;

import com.parkit.parkingsystem.config.DataBaseManager;
import com.parkit.parkingsystem.config.DataSourceFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.mockito.MockedStatic;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class H2DatabaseMock {

    private String createFilePath;
    private static boolean isInit = false;

    public H2DatabaseMock(String createFilePath) {
        this.createFilePath = createFilePath;
    }

    public void use(Callable<Void> test) throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        try(MockedStatic<DataSourceFactory> factoryMock = mockStatic(DataSourceFactory.class)) {
            dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=create schema if not exists test\\;");
            dataSource.setUser("user");
            dataSource.setPassword("password");
            factoryMock
                    .when(() -> DataSourceFactory.get(anyString(), anyString(), anyString()))
                    .thenReturn(dataSource);
            if (!isInit) {
                byte[] readAllBytes = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource(createFilePath).toURI()));
                String sql = new String(readAllBytes);
                DataBaseManager.INSTANCE.getConnection().prepareStatement(sql).executeUpdate();
                isInit = true;
            }
            test.call();
        }
    }
}
