package utils;

import java.io.IOException;
import java.util.logging.*;

public class GameLogger {
    // assumes the current class is called MyLogger
    private final static Logger logger = Logger.getLogger(GameLogger.class.getName());

    public static void logToFile(String s) {
        try {
            FileHandler fh = new FileHandler("log.txt", true);
            logger.addHandler(fh);
            logger.setLevel(Level.SEVERE);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.severe(s);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
