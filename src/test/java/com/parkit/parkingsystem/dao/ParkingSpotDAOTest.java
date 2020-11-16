package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.h2.H2DBConstants;
import com.parkit.parkingsystem.h2.H2DataBaseService;
import com.parkit.parkingsystem.h2.H2DatabaseMock;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpotDAOTest {

    private static ParkingSpotDAO parkingSpotDAO;
    private ParkingSpot parkingSpot;
    private static H2DatabaseMock dbMock = new H2DatabaseMock("test-data.sql");
    private static H2DataBaseService dbService = new H2DataBaseService();

    @BeforeEach
    void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
    }

    @AfterEach
    void afterEach() { dbService.clearDBTest(); }

    @Test
    void getNextAvailableSlotForBikeSuccess() throws Exception {
        dbMock.use(
                () -> {
                    assertEquals(4, parkingSpotDAO.getNextAvailableSlot(ParkingType.BIKE));
                    return null;
                }
        );
    }

    @Test
    void getNextAvailableSlotForCarSuccess() throws Exception {
        dbMock.use(
                () -> {
                    assertEquals(1, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
                    return null;
                }
        );
    }

    @Test
    void getNextAvailableSlotForCarFail() throws Exception {
        dbMock.use(
                () -> {
                    try (Connection con = dbService.getConnection();
                    PreparedStatement ps = con.prepareStatement(H2DBConstants.SET_AVAILABLE_FALSE)) {
                        ps.executeUpdate();
                        assertEquals(0, parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR));
                        return null;
                    }
                }
        );
    }

    @Test
    void updateParkingTestUpdate() throws Exception {
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
        dbMock.use(
                () -> {
                    assertTrue(parkingSpotDAO.updateParking(parkingSpot));
                    return null;
                }
        );
    }
}


