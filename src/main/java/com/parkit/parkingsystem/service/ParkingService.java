package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Service for Park.
 */
public class ParkingService {

    /**
     * @see Logger
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParkingService.class);

    /**
     * @see FareCalculatorService
     */
    FareCalculatorService fareCalculatorService = new FareCalculatorService();

    /**
     * @see InputReaderUtil
     */
    private InputReaderUtil inputReaderUtil;
    /**
     * @see ParkingSpotDAO
     */
    private ParkingSpotDAO parkingSpotDAO;
    /**
     * @see TicketDAO
     */
    TicketDAO ticketDAO;

    /**
     * Constructor.
     * @param pInputReaderUtil user selection
     * @param pParkingSpotDAO model parking spot
     * @param pTicketDAO model ticket
     */
    public ParkingService(final InputReaderUtil pInputReaderUtil,
                          final ParkingSpotDAO pParkingSpotDAO,
                          final TicketDAO pTicketDAO) {
        this.inputReaderUtil = pInputReaderUtil;
        this.parkingSpotDAO = pParkingSpotDAO;
        this.ticketDAO = pTicketDAO;
    }

    /**
     * Process when vehicle enter.
     */
    public void processIncomingVehicle() throws Exception {
        //TODO : si utilisateur récurrent, afficher un message d'accueil.
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();

            if (parkingSpot != null && parkingSpot.getId() > 0) {

                String vehicleRegNumber = getVehicleRegNumber();

                parkingSpot.setAvailable(false);

                parkingSpotDAO.updateParking(parkingSpot);
                //allot this parking space and mark it's availability as false

                LocalDateTime inTime = LocalDateTime.now();

                Ticket ticket = new Ticket();

                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                LOGGER.info("Generated Ticket and saved in DB");
                LOGGER.info(
                        "Please park your vehicle in spot number:"
                                + parkingSpot.getId());
                LOGGER.info("Recorded in-time for vehicle number:"
                        + vehicleRegNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
            throw e;
        }
    }

    /**
     * Get the vehicle registration number.
     * @return vehicle registration number
     * @throws Exception if vehicle reg number doesn't exist
     */
    private String getVehicleRegNumber() {
        LOGGER.info("Please type the vehicle registration number "
                + "and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * Get the next parking number available.
     * @return parking spot
     * @throws Exception if parking is full
     */
    public ParkingSpot getNextParkingNumberIfAvailable() throws Exception {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        ParkingType parkingType = getVehicleType();

        parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
        if (parkingNumber > 0) {
            parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
        } else {
            Exception exception = new Exception("Error "
                    + "fetching parking number from DB."
                    + "Parking slots might be full");
            LOGGER.error("Error fetching next available parking slot",
                    exception);
            throw exception;
        }
        return parkingSpot;
    }

    /**
     * get the vehicle type.
     * @return vehicle type
     */
    private ParkingType getVehicleType() {
        LOGGER.info("Please select vehicle type from menu");
        LOGGER.info("1 CAR");
        LOGGER.info("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: return ParkingType.CAR;
            case 2: return ParkingType.BIKE;
            default:
                LOGGER.info("Incorrect input provided");
                LOGGER.error("Error parsing user input for type of vehicle");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    /**
     * Process when vehicle exit.
     */
    public void processExitingVehicle() throws Exception {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            LocalDateTime outTime = LocalDateTime.now();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                LOGGER.info("Please pay the parking fare:"
                        + ticket.getPrice());
                LOGGER.info("Recorded out-time for vehicle number:"
                        + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                LOGGER.info("Unable to update ticket information."
                        + "Error occurred");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
            throw e;
        }
    }
}
