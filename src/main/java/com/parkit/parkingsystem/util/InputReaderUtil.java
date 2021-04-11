package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Input Reader Tools.
 */
public class InputReaderUtil {

    /**
     * @see Scanner
     */
    private Scanner scan;
    /**
     * @see Logger
     */
    private static final Logger LOGGER
            = LogManager.getLogger(InputReaderUtil.class);

    /**
     * Define Input origin at Scanner.
     * @param pScan Scanner
     */
    public InputReaderUtil(final Scanner pScan) {
        this.scan = pScan;
    }

    /**
     * Read Selection.
     * @return input
     */
    public int readSelection() {
        try {
            return Integer.parseInt(scan.nextLine());
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            LOGGER.info("Error reading input."
                    + "Please enter valid number for proceeding further");
            return -1;
        }
    }

    /**
     * Read the vehicle registration number.
     * @return vehicle registration number
     */
    public String readVehicleRegistrationNumber() {
        try {
            String vehicleRegNumber = scan.nextLine();
            if (vehicleRegNumber == null
                    || vehicleRegNumber.trim().length() == 0) {
                throw new IllegalArgumentException("Invalid input provided");
            }
            return vehicleRegNumber;
        } catch (Exception e) {
            LOGGER.error("Error while reading user input from Shell", e);
            LOGGER.info("Error reading input."
                    + "Please enter a valid vehicle registration number");
            throw e;
        }
    }
}
