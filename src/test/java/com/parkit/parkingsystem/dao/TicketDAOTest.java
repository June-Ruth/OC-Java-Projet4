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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TicketDAOTest {

    @Mock
    private DataSource ds;

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

        assertNotNull(ds);
        when(con.prepareStatement(any(String.class))).thenReturn(ps);
        when(dataBaseConfig.getConnection()).thenReturn(con);

        ticket = new Ticket();
        ticket.setInTime(LocalDateTime.now().minusMinutes(60));
        ticket.setOutTime(LocalDateTime.now());
        ticket.setVehicleRegNumber("ABC123DEF");
        ticket.setPrice(1.5);
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticket.setId(1);

        when(rs.first()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1);
        when(rs.getInt(2)).thenReturn(1);
        when(rs.getDouble(3)).thenReturn(1.5);
        when(rs.getTimestamp(4)).thenReturn(Timestamp.valueOf(LocalDateTime.now().minusMinutes(60)));
        when(rs.getTimestamp(5)).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(rs.getString(6)).thenReturn("CAR");
        when(ps.executeQuery()).thenReturn(rs);
    }

    @Disabled
    @Test
    public void saveTicketTestSuccess() {

        //assertThat(dataBaseConfig.closeConnection(con))

    }

    @Disabled
    @Test
    public void saveTicketTestFail() {

        //assertThat(dataBaseConfig.closeConnection(con))
    }

    @Test
    public void checkTicketByVehicleRegNumberTestTrue() throws SQLException {
        String vehicleRegNumber = "ABC123DEF";
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;
        when(rs.next()).thenReturn(true);
        assertTrue(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
    }

    @Test
    public void checkTicketByVehicleRegNumberTestFalse() throws SQLException {
        String vehicleRegNumber = "TEST FALSE";
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;
        when(rs.next()).thenReturn(false);
        assertFalse(ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
    }

    @Test
    public void checkTicketByVehicleRegNumberTestException() throws SQLException {
        String vehicleRegNumber = " ";
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseConfig;
        when(rs.next()).thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> ticketDAO.checkTicketByVehicleRegNumber(vehicleRegNumber));
    }

}
