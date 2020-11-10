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
    private static final Logger LOGGER = LogManager.getLogger(ParkingSpotDAO.class);

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
            ps.setString(1, parkingType.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = rs.getInt(1);
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
             PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT)) {

            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (SQLException ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        }
    }

}
