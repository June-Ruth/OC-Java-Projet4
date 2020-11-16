package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.parkit.parkingsystem.constants.ParkingType.TEST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
        fareCalculatorService.setTicketDAO(ticketDAO);
    }

    @Test
    public void calculateFareTestCar() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((1 * Fare.CAR_RATE_PER_HOUR),ticket.getPrice());
    }

    @Test
    public void calculateFareTestBike() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((1 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    public void calculateFareTestBikeWithFutureInTime() {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.plusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareTestBikeWithNullOutTime() {
        LocalDateTime inTime = LocalDateTime.now().plusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareTestBikeWithLessThanOneHourParkingTime() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(45);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    public void calculateFareTestCarWithLessThanOneHourParkingTime() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(45);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareTestCarWithMoreThanADayParkingTime() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(24*60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareTesUnknownType() {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, TEST, false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareTestForLessThan30min() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(15);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void calculateFareTestForRecurrentUserCar() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(anyString())).thenReturn(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR),ticket.getPrice());
    }

    @Test
    public void calculateFareTestForRecurrentUserBike() throws SQLException {
        LocalDateTime outTime = LocalDateTime.now();
        LocalDateTime inTime = outTime.minusMinutes(60);
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);
        String vehicleRegNumber = "ABC123DEF";

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(anyString())).thenReturn(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR),ticket.getPrice());
    }
}
