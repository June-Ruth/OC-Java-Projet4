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
     * @param id ID
     */
    public void setId(final int id) {
        this.id = id;
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
     * @param parkingSpot Parking Spot
     */
    public void setParkingSpot(final ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
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
     * @param vehicleRegNumber vehicle Number
     */
    public void setVehicleRegNumber(final String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
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
     * @param price price
     */
    public void setPrice(final double price) {
        this.price = price;
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
     * @param inTime arrive time
     */
    public void setInTime(final LocalDateTime inTime) {
        this.inTime = inTime;
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
     * @param outTime exit time
     */
    public void setOutTime(final LocalDateTime outTime) {
        this.outTime = outTime;
    }

    /**
     * Check Equality by ID.
     * @param o Ticket.
     * @return boolean true if equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    /**
     * Hash by Id to allow comparaison of Ticket.
     * @return int hash of ID.
     */
    @Override
    public int hashCode() {
        return  Objects.hash(id);
    }
}
