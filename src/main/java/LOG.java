package main.java;

import java.util.logging.*;

public class LOG {
    private static final Logger LOGGER = java.util.logging.Logger.getGlobal();

    public static void info(String message) {
        LOGGER.log(Level.INFO, message);
    }

    public static void warn(String message) {
        LOGGER.log(Level.WARNING, message);
    }

    public static void warn(String message, Throwable t) {
        LOGGER.log(Level.WARNING, message, t);
    }

    public static void error(String message) {
        LOGGER.log(Level.SEVERE, message);
    }

    public static void error(String message, Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }
}
