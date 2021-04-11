package com.parkit.parkingsystem.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InputReaderUtilTest {

    private static InputReaderUtil inputReaderUtil;

    private static final InputStream stdin = System.in;


    void provideInput(String inputString) {
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        Scanner scan = new Scanner(inputStream);
        inputReaderUtil = new InputReaderUtil(scan);
    }

    @AfterEach
    void restoreSystemInput() {
        System.setIn(stdin);
    }

    @Test
    void readSelectionTest1() {
        final String inputString = "1";
        provideInput(inputString);

        assertEquals(1, inputReaderUtil.readSelection());
    }

    @Test
    void readSelectionTestException() {
        final String inputString = "test";
        provideInput(inputString);

        assertEquals(-1, inputReaderUtil.readSelection());
    }

    @Test
    void readVehicleRegistrationNumberTestInvalid() {
        final String vehicleRegNumber = " ";
        provideInput(vehicleRegNumber);

        assertThrows(IllegalArgumentException.class, ()-> inputReaderUtil.readVehicleRegistrationNumber());
    }

    @Test
    void readVehicleRegistrationNumberTestValid() {
        final String vehicleRegNumber = "123ABC456";
        provideInput(vehicleRegNumber);

        assertEquals(vehicleRegNumber, inputReaderUtil.readVehicleRegistrationNumber());
    }
}
