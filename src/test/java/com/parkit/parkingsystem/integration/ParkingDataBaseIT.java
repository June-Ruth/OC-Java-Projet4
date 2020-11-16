package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.h2.H2DataBaseService;
import com.parkit.parkingsystem.h2.H2DatabaseMock;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static H2DatabaseMock dbMock = new H2DatabaseMock("test-data.sql");
    private static H2DataBaseService dbService = new H2DataBaseService();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private ParkingService parkingService;
    private FareCalculatorService fareCalculatorService;
    private static Ticket ticketInModel;
    private static Ticket ticketInOutModel;

    private final String vehicleRegNumber = "ABC123DEF";

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUpBeforeAll() {
        ticketInModel = new Ticket();
        ticketInModel.setId(2);
        ticketInModel.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, false));
        ticketInModel.setVehicleRegNumber("ABC123DEF");

        ticketInOutModel = new Ticket();
        ticketInOutModel.setId(2);
        ticketInOutModel.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticketInOutModel.setVehicleRegNumber("ABC123DEF");
        ticketInOutModel.setInTime(LocalDateTime.of(2019, 1, 2, 12, 0));
        ticketInOutModel.setOutTime(LocalDateTime.of(2019, 1, 2, 13, 0));
        ticketInOutModel.setPrice(1.5);
    }

    @BeforeEach
    private void setUpBeforeEach() {
        parkingSpotDAO = new ParkingSpotDAO();
        ticketDAO = new TicketDAO();
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        fareCalculatorService = new FareCalculatorService();
    }

    @AfterEach
    private void tearDown() {
        dbService.clearDBTest();
    }

    @Test
    public void testIncomingCarWithNonRecurrentUserIT() throws Exception {
        dbMock.use(
                () -> {
                    when(inputReaderUtil.readSelection()).thenReturn(1);
                    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
                    parkingService.processIncomingVehicle();
                    assertTrue(dbService.checkTicketInModelSave(vehicleRegNumber));
                    assertFalse(dbService.checkParkingSpotAvailability(1));
                    return null;
                }
        );
    }

    @Test
    public void testIncomingCarWithRecurrentUserIT() throws Exception {
        dbMock.use(
                () -> {
                    dbService.insertTicketInOutModel();
                    when(inputReaderUtil.readSelection()).thenReturn(1);
                    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
                    parkingService.processIncomingVehicle();
                    assertTrue(dbService.checkTicketInModelSave(vehicleRegNumber));
                    assertFalse(dbService.checkParkingSpotAvailability(1));
                    return null;
                }
        );
    }

    @Test
    public void testExitingCarWithNonRecurrentUserIT() throws Exception {
        dbMock.use(
                () -> {
                    dbService.insertTicketInModel();
                    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
                    parkingService.processExitingVehicle();
                    assertEquals(1.5, dbService.checkFareGenerated(vehicleRegNumber));
                    assertEquals(LocalDateTime.now().toLocalDate(), dbService.checkOutTimeGenerated(vehicleRegNumber).toLocalDate());
                    assertEquals(LocalDateTime.now().getHour(), dbService.checkOutTimeGenerated(vehicleRegNumber).getHour());
                    assertEquals(LocalDateTime.now().getMinute(), dbService.checkOutTimeGenerated(vehicleRegNumber).getMinute());
                    return null;
                }
        );
    }

    @Test
    public void testExitingCarWithRecurrentUserIT() throws Exception {
        dbMock.use(
                () -> {
                    dbService.insertTicketInOutModel();
                    dbService.insertTicketInModel();
                    when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn(vehicleRegNumber);
                    parkingService.processExitingVehicle();
                    assertEquals((1.5*0.95), dbService.checkFareGenerated(vehicleRegNumber));
                    assertEquals(LocalDateTime.now().toLocalDate(), dbService.checkOutTimeGenerated(vehicleRegNumber).toLocalDate());
                    assertEquals(LocalDateTime.now().getHour(), dbService.checkOutTimeGenerated(vehicleRegNumber).getHour());
                    assertEquals(LocalDateTime.now().getMinute(), dbService.checkOutTimeGenerated(vehicleRegNumber).getMinute());
                    return null;
                }
        );
    }
}
