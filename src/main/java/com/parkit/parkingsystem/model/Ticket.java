package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Ticket details.
 */
public class Ticket {
    /**
     * ID.
     */
    private int id;
    /**
     * @see ParkingSpot
     */
    private ParkingSpot parkingSpot;
    /**
     * Vehicle Number.
     */
    private String vehicleRegNumber;
    /**
     * Price.
     */
    private double price;
    /**
     * Arrive Time.
     */
    private LocalDateTime inTime;
    /**
     * Exit Time.
     */
    private LocalDateTime outTime;

    /**
     * Getter ID.
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter ID.
     * @param pId ID
     */
    public void setId(final int pId) {
        id = pId;
    }

    /**
     * Getter Parking Spot.
     * @return Parking Spot
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Setter ParingSpot.
     * @param pParkingSpot Parking Spot
     */
    public void setParkingSpot(final ParkingSpot pParkingSpot) {
        parkingSpot = pParkingSpot;
    }

    /**
     * Getter Vehicle Number.
     * @return Vehicle Number
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Setter Vehicle Number.
     * @param pVehicleRegNumber vehicle Number
     */
    public void setVehicleRegNumber(final String pVehicleRegNumber) {
        vehicleRegNumber = pVehicleRegNumber;
    }

    /**
     * Getter Price.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Setter price.
     * @param pPrice price
     */
    public void setPrice(final double pPrice) {
        price = pPrice;
    }

    /**
     * Getter Arrive Time.
     * @return arrive time
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Setter Arrive Time.
     * @param pInTime arrive time
     */
    public void setInTime(final LocalDateTime pInTime) {
        inTime = pInTime;
    }

    /**
     * Getter Exit Time.
     * @return exit time
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }
    /**
     * Setter Exit Time.
     * @param pOutTime exit time
     */
    public void setOutTime(final LocalDateTime pOutTime) {
        outTime = pOutTime;
    }

    /**
     * Check Equality by ID.
     * @param o Ticket.
     * @return boolean true if equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    /**
     * Hash by Id to allow compare of Ticket.
     * @return int hash of ID.
     */
    @Override
    public int hashCode() {
        return  Objects.hash(id);
    }
}
