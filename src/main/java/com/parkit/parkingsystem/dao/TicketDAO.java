package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
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

/**
 * Actions for tickets.
 */
public class TicketDAO {

    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
    /**
     * @see DataBaseConfig
     */
    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Save ticket.
     * @param ticket user ticket
     * @return false
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4,
                    Timestamp.valueOf(ticket.getInTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null
                    : (Timestamp.valueOf(ticket.getOutTime())));
            return ps.execute();
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Get the ticket.
     * @param vehicleRegNumber from user
     * @return ticket
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Connection con = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1),
                        ParkingType.valueOf(rs.getString(6)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(2));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(3));
                ticket.setInTime(rs.getTimestamp(4).toLocalDateTime());
                ticket.setOutTime(rs.getTimestamp(5).toLocalDateTime());
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
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
        Connection con = null;
        boolean isRecurrent = false;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    DBConstants.FIND_TICKET_BY_VEHICLE_REG_NUMBER);
            ps.setString(1, vehicleRegNumber);
            ResultSet rs = ps.executeQuery();
            isRecurrent = rs.next();
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (SQLException e) {
            LOGGER.error("Error checking recurrence", e);
            throw e;
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return isRecurrent;
    }

    /**
     * Update the ticket.
     * @param ticket from user
     * @return false
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, Timestamp.valueOf(ticket.getOutTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }
}
