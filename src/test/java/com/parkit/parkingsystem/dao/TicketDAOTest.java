package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    @Mock
    private Connection con;

    @Mock
    private DataBaseConfig dataBaseConfig;

    @Mock
    private PreparedStatement ps;

    @Mock
    private ResultSet rs;

    private TicketDAO ticketDAO;
    private Ticket ticket;

    @BeforeEach
    public void setUp() throws Exception {
        assertNotNull(dataBaseConfig);
        when(con.prepareStatement(any(String.class))).thenReturn(ps);
        when(dataBaseConfig.getConnection()).thenReturn(con);

        ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setOutTime(LocalDateTime.now());
        ticket.setVehicleRegNumber("ABC123DEF");
        ticket.setPrice(1.5);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR,true));
        ticket.setId(1);

        when(rs.getInt(1)).thenReturn(1);
        when(rs.getInt(2)).thenReturn(2);
        when(rs.getDouble(3)).thenReturn(1.5);
        when(rs.getTimestamp(4).toLocalDateTime()).thenReturn(LocalDateTime.now().minusMinutes(60));
        when(rs.getTimestamp(5).toLocalDateTime()).thenReturn(LocalDateTime.now());
        when(rs.getString(6)).thenReturn("CAR");
        when(ps.executeQuery()).thenReturn(rs);
    }

    @Disabled
    @Test
    //TODO
    public void checkTicketByVehicleRegNumberTestTrue(){
        String vehicleRegNumber = "ABC123DEF";
        ticketDAO = new TicketDAO();
        assertTrue(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
    }

    @Disabled
    @Test
    //TODO
    public void checkTicketByVehicleRegNumberTestFalse(){
        String vehicleRegNumber = "TEST FALSE";
        ticketDAO = new TicketDAO();
        assertFalse(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
    }

}
