package org.logfx.model;

import org.logfx.util.ScpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScpLogFile extends AbstractLogFile{
    private static final Pattern pattern = Pattern.compile("ssh://(.+):(.+)@([^:/]+)(?::(\\d+))?(/.+)");
    private final boolean isValidUri;
    private String localTargetFile;
    private ScpAccessInfo scpAccessInfo;
    private boolean refetchFlag = true;

    public ScpLogFile(String uri) {
        super(uri, LogFileType.SCP);

        isValidUri = parseUri(uri);
    }

    @Override
    public boolean isValid() {
        // ssh://user:password@10.80.0.69:22/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log
        Matcher matcher = pattern.matcher(uri);
        return matcher.matches();
    }

    private boolean parseUri(String uri) {
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            String user = matcher.group(1);
            String password = matcher.group(2);
            String host = matcher.group(3);
            //SSH default port is 22
            int port = matcher.group(4) != null? Integer.parseInt(matcher.group(4)) : 22;
            String path = matcher.group(5);
            scpAccessInfo = new ScpAccessInfo(user, password, host, port, path);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getLocalTargetFile() {
        if(refetchFlag) {
            localTargetFile = ScpUtil.fetchFile(scpAccessInfo);
            refetchFlag = false;
        }

        return localTargetFile;
    }

    public void setRefetchFlag(boolean refetchFlag) {
        System.out.println("refresh file: " + uri);
        this.refetchFlag = refetchFlag;
    }

    public static void main(String[] args) {
        String line = "ssh://user:password@10.80.0.69:/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log";

        Matcher matcher = pattern.matcher(line);
//        System.out.println(matcher.matches());

        if (matcher.find()) {
            System.out.println("g1: " + matcher.group(1));
            System.out.println("g2: " + matcher.group(2));
            System.out.println("g3: " + matcher.group(3));
            System.out.println("g4: " + matcher.group(4));
            System.out.println("g5: " + matcher.group(5));
        }

        System.out.println("matched:" + matcher.matches());
    }

}
