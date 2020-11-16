package com.parkit.parkingsystem.h2;

public class H2DBConstants {

    static final String INSERT_IN_OUT_TICKET = "insert into ticket (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) " +
            "values (1, 1, 'ABC123DEF', 1.5, PARSEDATETIME('2019-01-01 12:00', 'yyyy-MM-dd HH:mm'), PARSEDATETIME('2019-01-01 13:00', 'yyyy-MM-dd HH:mm'))";

    static final String INSERT_IN_TICKET =
            "insert into ticket (ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME) values (2, 3, 'ABC123DEF', null, ?, null)";

    static final String SET_AVAILABLE_TRUE =
            "update parking set available = true";

    static final String CLEAR_TICKET_TABLE =
            "truncate table ticket";

    static final String SET_PARKING_3_FALSE =
            "update parking set available = false where PARKING_NUMBER = 3";

    static final String CHECK_TICKET_IN_SAVE =
            "select * from ticket where VEHICLE_REG_NUMBER=? and " +
                    "OUT_TIME IS NULL ";

    static final String CHECK_AVAILABILITY_SAVE =
            "select p.AVAILABLE from parking p " +
                    "where p.PARKING_NUMBER = ?";

    static final String CHECK_FARE =
            "select t.PRICE from ticket t ";
}
