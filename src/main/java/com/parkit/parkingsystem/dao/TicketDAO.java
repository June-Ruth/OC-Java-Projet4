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
     * Save ticket.
     * @param ticket user ticket
     * @return false
     */
    public boolean saveTicket(final Ticket ticket) {
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.SAVE_TICKET)) {
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setNull(3, Types.DOUBLE);
            ps.setTimestamp(4,
                    Timestamp.valueOf(ticket.getInTime()));
            ps.setNull(5, Types.TIMESTAMP);
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
                    ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                            ParkingType.valueOf(rs.getString(6)), false);
                    ticket.setParkingSpot(parkingSpot);
                    ticket.setId(rs.getInt(2));
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(rs.getDouble(3));
                    ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
                    Timestamp timestamp = rs.getTimestamp(5);
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
            final String vehicleRegNumber) throws SQLException {
        boolean isRecurrent = false;
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.FIND_TICKET_BY_VEHICLE_REG_NUMBER)) {
            ps.setString(1, vehicleRegNumber);
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
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
            ps.setInt(3, ticket.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            LOGGER.error("Error saving ticket info", ex);
        }
        return false;
    }
}
