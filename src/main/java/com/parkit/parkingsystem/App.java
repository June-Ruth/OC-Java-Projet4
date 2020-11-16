package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Start App.
 */
public final class App {
    private App() { }
    /**
     * @see Logger
     */
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    /**
     * Main.
     * @param args arg
     */
    public static void main(final String[] args) {
        LOGGER.info("Initializing Parking System");
        try {
            InteractiveShell.loadInterface();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
