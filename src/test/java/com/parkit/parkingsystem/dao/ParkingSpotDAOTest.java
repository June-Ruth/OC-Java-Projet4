package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseManager;
import com.parkit.parkingsystem.config.DataSourceFactory;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.h2.H2DataBaseService;
import com.parkit.parkingsystem.h2.H2DatabaseMock;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.test.TestAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private ParkingSpot parkingSpot;
    private static H2DatabaseMock dbMock = new H2DatabaseMock("test-data.sql");
    private static H2DataBaseService dbService = new H2DataBaseService();

    @BeforeEach
    public void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
    }

    @AfterEach
    public void afterEach() { dbService.clearDBTest(); }

    @Test
    public void getNextAvailableSlotForBikeSuccess() throws Exception {
        dbMock.use(
                () -> {
                    assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
                    return null;
                }
        );
    }

    @Test
    public void getNextAvailableSlotForCarSuccess() throws Exception {
        dbMock.use(
                () -> {
                    assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
                    return null;
                }
        );
    }

    @Test
    public void getNextAvailableSlotForCarFail() throws Exception {
        dbMock.use(
                () -> {
                    final String SET_AVAILABLE_FALSE = "update parking set available = false";
                    try (Connection con = dbService.getConnection();
                    PreparedStatement ps = con.prepareStatement(SET_AVAILABLE_FALSE)) {
                        ps.executeUpdate();
                        assertEquals(0, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
                        return null;
                    }
                }
        );
    }

    @Test
    public void updateParkingTestUpdate() throws Exception {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        dbMock.use(
                () -> {
                    assertTrue(parkingSpotDAO.updateParking(parkingSpot));
                    return null;
                }
        );
    }

    @Disabled //TODO
    @Test
    public void updateParkingTestUnupdate() throws Exception {
        dbMock.use(
                () -> {
                    //Générer une exception SQL dans l'Update.
                    assertFalse(parkingSpotDAO.updateParking(parkingSpot));
                    return null;
                }
        );
    }
}


