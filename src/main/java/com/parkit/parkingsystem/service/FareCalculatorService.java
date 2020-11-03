package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.Ticket;

import java.time.Duration;

/**
 * Service for fare calculator.
 */
public class FareCalculatorService {

    /**
     * Calculate fare.
     * If user stay less or equal to 30 min, price will be 0.
     * If vehicleRegNumber is already in DB, then price will be 5% reducing.
     * //TODO
     * @param ticket from user
     */
    public void calculateFare(final Ticket ticket) {

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

        // si checkTicketByVehicleRegNumber is true alors set price - 5%

        } else {
            switch (parkingType) {
                case CAR:
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                case BIKE:
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
    }
}
