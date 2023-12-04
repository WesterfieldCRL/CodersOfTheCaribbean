package Util;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

/**
 * The {@code AppLogger} in the {@code Util} package provides a static logging utility for the application.
 * It initializes a {@code Logger} instance that can be used across the application
 * to log important information and exceptions.
 *
 * <p>It is set up with a {@code FileHandler} to write logs to a file named 'AppLog.log'.
 * The logger is configured to append logs to the existing file and to use a {@code SimpleFormatter}
 * to format the log messages.</p>
 *
 */
public class AppLogger {
    private static Logger logger = Logger.getLogger("AppLogger");
    private static FileHandler fh;

    static {
        try {
            fh = new FileHandler("AppLog.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info("Logger initialized");
        } catch (SecurityException | IOException e) {
            logger.log(Level.SEVERE, "Logger could not be initialized", e);
        }
    }

    /**
     * Retrieves the static instance of the {@code Logger} for the application.
     *
     * <p>This method provides access to the centralized logger instance that has been
     * set up for the application. It can be used to log messages, warnings, and errors.</p>
     *
     * @return the initialized {@code Logger} instance
     */
    public static Logger getLogger() {
        return logger;
    }
}
