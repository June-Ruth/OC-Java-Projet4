package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseManager;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

/**
 * Actions for tickets.
 */
public class TicketDAO {

    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(TicketDAO.class);

    /**
     * Constant one.
     */
    private static final byte ONE = 1;
    /**
     * Constant two.
     */
    private static final byte TWO = 2;
    /**
     * Constant three.
     */
    private static final byte THREE = 3;
    /**
     * Constant four.
     */
    private static final byte FOUR = 4;
    /**
     * Constant five.
     */
    private static final byte FIVE = 5;
    /**
     * Constant six.
     */
    private static final byte SIX = 6;

    /**
     * Save ticket.
     * @param ticket user ticket
     * @return false
     */
    public boolean saveTicket(final Ticket ticket) {
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.SAVE_TICKET)) {
            ps.setInt(ONE, ticket.getParkingSpot().getId());
            ps.setString(TWO, ticket.getVehicleRegNumber());
            ps.setNull(THREE, Types.DOUBLE);
            ps.setTimestamp(FOUR,
                    Timestamp.valueOf(ticket.getInTime()));
            ps.setNull(FIVE, Types.TIMESTAMP);
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            LOGGER.error("Error fetching next available slot", ex);
        }
        return false;
    }

    /**
     * Get the ticket.
     * @param vehicleRegNumber from user
     * @return ticket
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Ticket ticket = null;
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(DBConstants.GET_TICKET)) {
            ps.setString(1, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ticket = new Ticket();
                    ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(ONE),
                            ParkingType.valueOf(rs.getString(SIX)), false);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setId(rs.getInt(TWO));
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(rs.getDouble(THREE));
                    ticket.setInTime(rs.getTimestamp(FOUR).toLocalDateTime());
                    Timestamp timestamp = rs.getTimestamp(FIVE);
                    ticket.setOutTime(timestamp != null
                            ? timestamp.toLocalDateTime() : null);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching ticket for " + vehicleRegNumber, ex);
        }
        return ticket;
    }

    /**
     * Find ticket by Vehicle Reg Number.
     * @param vehicleRegNumber from user
     * @return true if one ticket contains vehicle reg number
     */
    public boolean checkTicketByVehicleRegNumber(
            final String vehicleRegNumber) {
        boolean isRecurrent = false;
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.FIND_TICKET_BY_VEHICLE_REG_NUMBER)) {
            ps.setString(ONE, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
                isRecurrent = rs.next();
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking recurrence", e);
        }
        return isRecurrent;
    }

    /**
     * Update the ticket.
     * @param ticket from user
     * @return false
     */
    public boolean updateTicket(final Ticket ticket) {
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.UPDATE_TICKET)) {
            ps.setDouble(ONE, ticket.getPrice());
            ps.setTimestamp(TWO, Timestamp.valueOf(ticket.getOutTime()));
            ps.setInt(THREE, ticket.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            LOGGER.error("Error saving ticket info", ex);
        }
        return false;
    }
}
