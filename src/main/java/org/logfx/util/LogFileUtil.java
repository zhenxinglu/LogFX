package org.logfx.util;

import org.logfx.model.AbstractLogFile;
import org.logfx.model.LocalLogFile;
import org.logfx.model.ScpLogFile;

public class LogFileUtil {

    public static AbstractLogFile parse(String uri) {
        uri = uri.trim();
        return uri.startsWith("ssh://") ? new ScpLogFile(uri) : new LocalLogFile(uri);
    }
}
