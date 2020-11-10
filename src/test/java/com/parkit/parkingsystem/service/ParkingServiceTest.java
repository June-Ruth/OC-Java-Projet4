package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.test.TestAppender;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    private  static TestAppender appender;

    private static Logger logger = LogManager.getLogger(ParkingService.class);

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;
    @Mock
    private static FareCalculatorService fareCalculatorService;

    @BeforeAll
    public static void setUpBeforeAll() {
        appender = new TestAppender();
        ((org.apache.logging.log4j.core.Logger)logger).addAppender(appender);
    }

    @BeforeEach
    public void setUpBeforeEach() {
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.fareCalculatorService = fareCalculatorService;
    }

    @AfterEach
    public void cleanUp() {
        appender.reset();
    }

    @Test
    public void processIncomingVehicleTestCar() throws Exception {
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,false));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(LocalDateTime.now());
        ticket.setOutTime(null);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);

        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }

    @Test
    public void processIncomingVehicleTestException() {
        assertThrows(Exception.class, ()-> parkingService.processIncomingVehicle());
    }

    @Test
    public void getNextParkingNumberIfAvailableTestCar() throws Exception{
        ParkingType parkingType = ParkingType.CAR;
        ParkingSpot parkingSpot =  new ParkingSpot(1, parkingType, true);

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);

        assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableTestBike() throws Exception {
        ParkingType parkingType = ParkingType.BIKE;
        ParkingSpot parkingSpot =  new ParkingSpot(1, parkingType, true);

        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(1);

        assertEquals(parkingSpot, parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableTestDefault(){
        when(inputReaderUtil.readSelection()).thenReturn(3);

        assertThrows(IllegalArgumentException.class, () -> parkingService.getNextParkingNumberIfAvailable());
    }

    @Test
    public void getNextParkingNumberIfAvailableTestCarNotAvailable() throws SQLException {
        ParkingType parkingType = ParkingType.CAR;

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(parkingType)).thenReturn(-1);

        assertThrows(Exception.class, () -> parkingService.getNextParkingNumberIfAvailable());
    }


    @Test
    public void processExitingVehicleTestCar() throws Exception{
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        doNothing().when(fareCalculatorService).calculateFare(any(Ticket.class));

        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void processExitingVehicleTestException() {
            assertThrows(NullPointerException.class, ()-> parkingService.processExitingVehicle());
    }

    @Test
    public void processExitingVehicleTestElseCase() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setParkingSpot(parkingSpot);

        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();
        assertEquals(2, appender.getLogCount());
    }

}
