package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;

/**
 * Service for fare calculator.
 */
public class FareCalculatorService {

    /**
     * @see TicketDAO
     */
    private TicketDAO ticketDAO = new TicketDAO();

    /**
     * get access to TicketDAO.
     * @return ticketDAO
     */
    TicketDAO getTicketDAO() {
        return ticketDAO;
    }

    /**
     * set access to ticketDAO.
     * @param pTicketDAO ticket DAO
     */
    void setTicketDAO(final TicketDAO pTicketDAO) {
        ticketDAO = pTicketDAO;
    }


    /**
     * Calculate fare.
     * If user stay less or equal to 30 min, price will be 0.
     * If vehicleRegNumber is already in DB, then price will be 5% reducing.
     * @param ticket from user
     */
    void calculateFare(final Ticket ticket) {

        if (ticket.getOutTime() == null
                || ticket.getOutTime().isBefore(ticket.getInTime())) {
            throw new IllegalArgumentException("Incorrect OutTime provided.");
        }

        final int toHours = 60;
        double duration = (double) (Duration.between(
                ticket.getInTime(), ticket.getOutTime()).toMinutes()) / toHours;

        ParkingType parkingType = ticket.getParkingSpot().getParkingType();

        final double freeDuration = 0.5;
        if (duration <= freeDuration) {
            ticket.setPrice(0);
        } else {
            final double discountRate = 0.95;
            final double usualRate = 1;
            final double rate = ticketDAO.checkTicketByVehicleRegNumber(ticket.
                    getVehicleRegNumber()) ? (discountRate) : (usualRate);
            switch (parkingType) {
                case CAR:
                    final double priceCar =
                            duration * rate * Fare.CAR_RATE_PER_HOUR;
                    ticket.setPrice(priceCar);
                    break;
                case BIKE:
                    final double priceBike =
                            duration * rate * Fare.BIKE_RATE_PER_HOUR;
                    ticket.setPrice(priceBike);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}
