package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.h2.H2DatabaseMock;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.test.TestAppender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static H2DatabaseMock dbMock = new H2DatabaseMock("test-data.sql");

    private TicketDAO ticketDAO;
    private static Ticket ticketInOutModel;
    private static Ticket ticketInModel;
    private static Logger logger = LogManager.getLogger(TicketDAO.class);
    private  static TestAppender appender;

    @BeforeAll
    public static void setUpBeforeAll() {
        appender = new TestAppender();
        ((org.apache.logging.log4j.core.Logger)logger).addAppender(appender);

        ticketInOutModel = new Ticket();
        ticketInOutModel.setId(1);
        ticketInOutModel.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticketInOutModel.setVehicleRegNumber("ABC123DEF");
        ticketInOutModel.setPrice(1.5);
        ticketInOutModel.setInTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        ticketInOutModel.setOutTime(LocalDateTime.of(2020, 1, 1, 13, 0));

        ticketInModel = new Ticket();
        ticketInModel.setId(2);
        ticketInModel.setParkingSpot(new ParkingSpot(3, ParkingType.CAR, false));
        ticketInModel.setVehicleRegNumber("ABC123DEF");
        ticketInModel.setInTime(LocalDateTime.of(2020, 1, 2, 12, 0));
    }

    @BeforeEach
    public void setUp() {
        ticketDAO = new TicketDAO();
    }

    @AfterEach
    public void cleanUp() {
        appender.reset();
    }

    @Test
    public void saveTicketTestSuccess() throws Exception {
        Ticket newTicket = new Ticket();
        //newTicket.setId(3);
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
    public void saveTicketTestFail() throws Exception {
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
    public void getTicketTestSuccess() throws Exception {
        String vehicleRegNumber = "ABC123DEF";
        dbMock.use(
                () -> {
                    assertEquals(ticketInModel, ticketDAO.getTicket(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    //NB : pourrait ajouter un message d'erreur pour signaler à l'utilisateur que son numéro de véhicule n'est pas reconnu
    public void getTicketTestWithNonExistingVehicleRegNumber() throws Exception {
        String vehicleRegNumber = "GHI456JKL";
        dbMock.use(
                () -> {
                    assertNull(ticketDAO.getTicket(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Disabled //TODO
    @Test
    public void getTicketTestException() throws Exception {
        String vehicleRegNumber = "ABC 123 DEF";
        dbMock.use(
                () -> {
                    ticketDAO.getTicket(vehicleRegNumber);
                    assertEquals(1, appender.getLogCount());
                    return null;
                }
        );
    }

    @Test
    public void checkTicketByVehicleRegNumberTestTrue() throws Exception {
        String vehicleRegNumber = "ABC123DEF";
        dbMock.use(
                () -> {
                    assertTrue(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Test
    public void checkTicketByVehicleRegNumberTestFalse() throws Exception {
        String vehicleRegNumber = "GHI456JKL";
        dbMock.use(
                () -> {
                    assertFalse(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
                    return null;
                }
        );
    }

    @Disabled //TODO
    @Test
    public void checkTicketByVehicleRegNumberTestException() throws Exception {
        String vehicleRegNumber = " ";
        dbMock.use(
                () -> {
                    ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber);
                    assertEquals(1, appender.getLogCount());
                    return null;
                }
        );
    }

    @Test
    public void updateTicketTestSuccess() throws Exception {
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
    public void updateTicketTestFail() throws Exception {
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

    @Disabled //TODO
    @Test
    public void updateTicketTestException() throws Exception {
        Ticket newTicket = new Ticket();
        newTicket.setOutTime(LocalDateTime.of(2020, 2, 1, 13, 0));
        newTicket.setId(-1);
        dbMock.use(
                () -> {
                    ticketDAO.updateTicket(newTicket);
                    assertEquals(1, appender.getLogCount());
                    return null;
                }
        );
    }

}
