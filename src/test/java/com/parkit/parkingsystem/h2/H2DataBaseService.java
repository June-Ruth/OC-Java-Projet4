package com.parkit.parkingsystem.h2;

import com.parkit.parkingsystem.config.DataBaseManager;
import com.parkit.parkingsystem.constants.DBConstants;

import java.sql.*;
import java.time.LocalDateTime;

public class H2DataBaseService {

    public Connection getConnection() {
        return DataBaseManager.INSTANCE.getConnection();
    }

    public void setUpBeforeUnitTestTicketDAO() {
        try (Connection con = getConnection();
             PreparedStatement ps1 = con.prepareStatement(H2DBConstants.INSERT_IN_OUT_TICKET);
             PreparedStatement ps2 = con.prepareStatement(H2DBConstants.INSERT_IN_TICKET);
             PreparedStatement ps3 = con.prepareStatement(H2DBConstants.SET_PARKING_3_FALSE)) {
            ps2.setTimestamp(1, Timestamp.valueOf(LocalDateTime.of(2020, 1, 2, 12, 0)));
            ps1.execute();
            ps2.execute();
            ps3.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearDBTest() {
        try (Connection con = getConnection();
             PreparedStatement ps1 = con.prepareStatement(H2DBConstants.SET_AVAILABLE_TRUE);
             PreparedStatement ps2 = con.prepareStatement(H2DBConstants.CLEAR_TICKET_TABLE)) {
            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTicketInModel() {
        try (Connection con = getConnection();
             PreparedStatement ps1 = con.prepareStatement(H2DBConstants.INSERT_IN_TICKET);
             PreparedStatement ps2 = con.prepareStatement(H2DBConstants.SET_PARKING_3_FALSE)) {
            ps1.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusMinutes(60)));
            ps1.executeUpdate();
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertTicketInOutModel() {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(H2DBConstants.INSERT_IN_OUT_TICKET)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkTicketInModelSave(String vehicleRegNumber) {
        boolean result = false;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(H2DBConstants.CHECK_TICKET_IN_SAVE)) {
            ps.setString(1, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
                result = rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkParkingSpotAvailability(int parkingNumber) {
        boolean result = false;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(H2DBConstants.CHECK_AVAILABILITY_SAVE)) {
            ps.setInt(1, parkingNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) result = rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public double checkFareGenerated(String vehicleRegNumber) {
        double fare = 0.0d;
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET)){
            ps.setString(1, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
               if (rs.next()) fare = rs.getDouble(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fare;
    }

    public LocalDateTime checkOutTimeGenerated(String vehicleRegNumber) {
        LocalDateTime outTime = null;
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET)){
            ps.setString(1, vehicleRegNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) outTime = rs.getTimestamp(5).toLocalDateTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return outTime;
    }
}
