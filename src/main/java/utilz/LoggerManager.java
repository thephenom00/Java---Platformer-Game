package utilz;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager {
    private boolean enableLogger;
    private Logger logger;

    public LoggerManager(boolean enableLogger) {
        this.enableLogger = enableLogger;
        this.logger = Logger.getLogger(LoggerManager.class.getName());
        setupLogger();
    }

    private void setupLogger() {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        logger.addHandler(consoleHandler);

        // Disable parent loggers to prevent duplicate logs
        Logger parentLogger = logger.getParent();
        while (parentLogger != null) {
            parentLogger.setUseParentHandlers(false);
            for (Handler handler : parentLogger.getHandlers()) {
                parentLogger.removeHandler(handler);
            }
            parentLogger = parentLogger.getParent();
        }
    }

    public void log(String message) {
        if (enableLogger) {
            logger.info(message);
        }
    }

    public boolean isEnabled() {
        return enableLogger;
    }
}
