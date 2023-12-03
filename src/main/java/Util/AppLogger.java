package Util;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

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

    public static Logger getLogger() {
        return logger;
    }
}
