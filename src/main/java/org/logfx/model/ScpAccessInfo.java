package org.logfx.model;

public class ScpAccessInfo {
    private String user;
    private String password;
    private String host;
    private int port;
    private String filePath;


    public ScpAccessInfo(String user, String password, String host, int port, String filePath) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.filePath = filePath;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getFilePath() {
        return filePath;
    }
}
