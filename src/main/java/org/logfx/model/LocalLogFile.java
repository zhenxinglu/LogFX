package org.logfx.model;

import java.io.File;

public class LocalLogFile extends AbstractLogFile{

    public LocalLogFile(String uri) {
        super(uri, LogFileType.LOCAL);
    }

    @Override
    public boolean isValid() {
        File file = new File(uri);
        return file.exists() && !file.isDirectory();
    }

    @Override
    public String getLocalTargetFile() {
        return uri;
    }
}
