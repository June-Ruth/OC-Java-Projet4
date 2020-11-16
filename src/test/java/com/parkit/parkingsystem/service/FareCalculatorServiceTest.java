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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;

    private LocalDateTime outTime;
    private LocalDateTime inTime;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber = "ABC123DEF";

    @Mock
    private static TicketDAO ticketDAO;

    @BeforeAll
    static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    void setUpPerTest() {
        ticket = new Ticket();
        fareCalculatorService.setTicketDAO(ticketDAO);
    }

    @Test
    void calculateFareTestCar() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((1 * Fare.CAR_RATE_PER_HOUR),ticket.getPrice());
    }

    @Test
    void calculateFareTestBike() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((1 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice());
    }

    @Test
    void calculateFareTestBikeWithFutureInTime() {
        outTime = LocalDateTime.now();
        inTime = outTime.plusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    void calculateFareTestBikeWithNullOutTime() {
        outTime = null;
        inTime = LocalDateTime.now().plusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setParkingSpot(parkingSpot);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    void calculateFareTestBikeWithLessThanOneHourParkingTime() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(45);
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
    }

    @Test
    void calculateFareTestCarWithLessThanOneHourParkingTime() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(45);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    void calculateFareTestCarWithMoreThanADayParkingTime() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(24*60);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber)).thenReturn(false);

        fareCalculatorService.calculateFare(ticket);

        assertEquals( (24 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    void calculateFareTestForLessThan30min() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(15);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
    }

    @Test
    void calculateFareTestForRecurrentUserCar() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(anyString())).thenReturn(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * Fare.CAR_RATE_PER_HOUR),ticket.getPrice());
    }

    @Test
    void calculateFareTestForRecurrentUserBike() {
        outTime = LocalDateTime.now();
        inTime = outTime.minusMinutes(60);
        parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber(vehicleRegNumber);

        when(ticketDAO.checkTicketByVehicleRegNumber(anyString())).thenReturn(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * Fare.BIKE_RATE_PER_HOUR),ticket.getPrice());
    }
}
