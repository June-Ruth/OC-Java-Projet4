package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseManager;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Actions for parking spot.
 */
public class ParkingSpotDAO {

    /**
     * @see Logger
     */
    private static final Logger LOGGER =
            LogManager.getLogger(ParkingSpotDAO.class);
    /**
     * Constant one.
     */
    private static final byte ONE = 1;
    /**
     * Constant two.
     */
    private static final byte TWO = 2;

    /**
     * Get the next available spot to park.
     * @param parkingType car or bike
     * @return spot number available
     */
    public int getNextAvailableSlot(final ParkingType parkingType) {
        int result = -1;
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT)) {
            ps.setString(ONE, parkingType.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(ONE);
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("Error fetching next available slot", ex);
        }
        return result;
    }

    /**
     * Update the availability for that parking spot.
     * @param parkingSpot parking spot concerned
     * @return available as false
     */
    public boolean updateParking(final ParkingSpot parkingSpot) {
        try (Connection con = DataBaseManager.INSTANCE.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     DBConstants.UPDATE_PARKING_SPOT)) {

            ps.setBoolean(ONE, parkingSpot.isAvailable());
            ps.setInt(TWO, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        }
    }

}
