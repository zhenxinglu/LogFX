package org.logfx.util;

import org.apache.tools.ant.taskdefs.optional.ssh.Scp;
import org.apache.tools.ant.Project;
import org.logfx.model.ScpAccessInfo;
import org.logfx.model.ScpLogFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class ScpUtil {

    public static String fetchFile(ScpAccessInfo accessInfo) {
        // ssh://user:password@10.80.0.69:22/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log
        //   C-TCS_log_tcs.hfcim.adapter.gantry.log     C-TCS_log_tcs.hfcim.adapter.extender.log
        Scp scp = new Scp();

        Path localFile;
        try {
            localFile = Files.createTempFile("logfx", ".log");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        String localFileStr = localFile.toFile().getAbsolutePath();
        scp.setPort(accessInfo.getPort());
        scp.setLocalTofile(localFileStr);
//        scp.setSftp(true);
        scp.setRemoteFile(accessInfo.getUser() + ":" + accessInfo.getPassword() + "@" + accessInfo.getHost() + ":" + accessInfo.getFilePath());
        scp.setProject(new Project());
        scp.setTrust(true);
        scp.execute();
        return localFileStr;
    }

    public static void main(String[] args) {
        ScpAccessInfo accessInfo = new ScpAccessInfo("root", "root12", "10.80.0.69", 22, "/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log");
        ScpUtil.fetchFile(accessInfo);
        System.out.println("main");
    }
}
