package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.h2.H2DataBaseService;
import com.parkit.parkingsystem.h2.H2DatabaseMock;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {

    private static H2DatabaseMock dbMock = new H2DatabaseMock("test-data.sql");
    private static H2DataBaseService dbService = new H2DataBaseService();

    private TicketDAO ticketDAO;
    private static Ticket ticketInModel;

    @BeforeAll
    static void setUpBeforeAll() throws Exception {
        dbMock.use(
                () -> {
                    dbService.setUpBeforeUnitTestTicketDAO();
                    return null;
                }
        );
        ticketInModel = new Ticket();
        ticketInModel.setId(2);
        ticketInModel.setParkingSpot(new ParkingSpot(3, ParkingType.CAR, false));
        ticketInModel.setVehicleRegNumber("ABC123DEF");
        ticketInModel.setInTime(LocalDateTime.of(2020, 1, 2, 12, 0));
    }

    @BeforeEach
    void setUp() {
        ticketDAO = new TicketDAO();
    }

    @AfterAll
    static void tearDown() {
        dbService.clearDBTest();
    }

    @Test
    void saveTicketTestSuccess() throws Exception {
        Ticket newTicket = new Ticket();
        newTicket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, true));
        newTicket.setVehicleRegNumber("123NEW456");
        newTicket.setInTime(LocalDateTime.of(2020, 2,3, 12, 0));
        dbMock.use(
                () -> {
                    assertTrue(ticketDAO.saveTicket(newTicket));
                    return null;
                }
        );
    }

    @Test
    void saveTicketTestFail() throws Exception {
        Ticket newTicket = new Ticket();
        newTicket.setParkingSpot(new ParkingSpot(2, ParkingType.CAR, true));
        newTicket.setVehicleRegNumber(null);
        newTicket.setInTime(LocalDateTime.of(2020, 2,3, 12, 0));
        dbMock.use(
                () -> {
                    assertFalse(ticketDAO.saveTicket(newTicket));
                    return null;
                }
        );
    }

    @Test
    void getTicketTestSuccess() throws Exception {
        String vehicleRegNumber = "ABC123DEF";
        dbMock.use(
                () -> {
                    assertEquals(ticketInModel, ticketDAO.getTicket(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    void getTicketTestWithNonExistingVehicleRegNumber() throws Exception {
        String vehicleRegNumber = "GHI456JKL";
        dbMock.use(
                () -> {
                    assertNull(ticketDAO.getTicket(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    void checkTicketByVehicleRegNumberTestTrue() throws Exception {
        String vehicleRegNumber = "ABC123DEF";
        dbMock.use(
                () -> {
                    assertTrue(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    void checkTicketByVehicleRegNumberTestFalse() throws Exception {
        String vehicleRegNumber = "GHI456JKL";
        dbMock.use(
                () -> {
                    assertFalse(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    void updateTicketTestSuccess() throws Exception {
        ticketInModel.setPrice(1.5);
        ticketInModel.setOutTime(LocalDateTime.of(2020, 1, 1, 13, 0));
        dbMock.use(
                () -> {
                    assertTrue(ticketDAO.updateTicket(ticketInModel));
                    return null;
                }
        );
    }

    @Test
    void updateTicketTestFail() throws Exception {
        Ticket newTicket = new Ticket();
        newTicket.setOutTime(LocalDateTime.of(2020, 2, 1, 13, 0));
        newTicket.setId(4543);
        dbMock.use(
                () -> {
                    assertFalse(ticketDAO.updateTicket(newTicket));
                    return null;
                }
        );
    }
}
