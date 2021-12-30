package org.logfx;


import java.util.Arrays;

enum Level {
    DEBUG, INFO, WARN, ERROR, FATAL, TRACE, UNKNOWN;

    public static Level parse(String value) {
        return Arrays.stream(Level.values()).filter(l -> l.name().equals(value)).findAny().orElse(UNKNOWN);
    }
}

public class LogRecord {
    private String  timestamp;
    private Level  level;
    private String message ="";

    public LogRecord(String timestamp, Level level, String message) {
        this.timestamp = timestamp;
        this.level     = level;
        this.message   = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public void appendToMessage(String appendedMsg) {
        message = new StringBuilder(message).append("\n").append(appendedMsg).toString();
    }

}


