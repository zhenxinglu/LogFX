package org.logfx.model;

public abstract class AbstractLogFile {
    protected final String uri;
    protected LogFileType logFileType;

    public AbstractLogFile(String uri, LogFileType logFileType) {
        this.uri = uri;
        this.logFileType = logFileType;
    }

    public abstract boolean isValid();

    public LogFileType getLogFileType() {
        return logFileType;
    }

    public abstract String getLocalTargetFile();
}
