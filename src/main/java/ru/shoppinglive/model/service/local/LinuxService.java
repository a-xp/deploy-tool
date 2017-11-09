package ru.shoppinglive.model.service.local;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rkhabibullin on 29.03.2017.
 */
@Service
@Profile("!local")
public class LinuxService extends OsService {
    private static final Logger log = LoggerFactory.getLogger("application");

    @Override
    public int getPortByPid(int pid) {
        ProcessBuilder pb = new ProcessBuilder("netstat", "-lnp", "--tcp");
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader processOutputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while (( readLine = processOutputReader.readLine()) != null){
                    String[] parts = readLine.split("\\s+");
                    if(parts.length>=7 && parts[6].matches(pid+"/.*")){
                        String[] addrPart = parts[3].split(":");
                        return Integer.parseInt(addrPart[addrPart.length-1]);
                    }
                }
                process.waitFor(5, TimeUnit.SECONDS);
                if(process.isAlive())process.destroyForcibly();
            }
        }catch (IOException | InterruptedException e){
            log.warn("netstat read failed " + e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean run(String name, String version) {
        downloadJar(name, version);
        return serviceCommand(name, "start", version, "started");
    }

    @Override
    public boolean killProcess(int pid) {
        ProcessBuilder pb = new ProcessBuilder("kill", String.valueOf(pid));
        pb.redirectErrorStream();
        try {
            Process process = pb.start();
            process.waitFor(5, TimeUnit.SECONDS);
            if(process.isAlive())process.destroyForcibly();
            return true;
        }catch (Exception e){
            log.warn("Process kill failed "+e.getMessage());
            return false;
        }
    }

    @Override
    public boolean stopService(String code, String version) {
        return serviceCommand(code, "stop", version, "stopped");
    }

    @Override
    public boolean runCron(String code, String version, String params) {
        Path shFile = Paths.get(soaRoot.getPath(), "cron", code+".sh");
        if(Files.exists(shFile)){
            log.info("run cron "+code+":"+version+" with params "+params);
            ProcessBuilder pb = new ProcessBuilder(shFile.toString(), version, params);
            try {
                Process process = pb.start();
                try (BufferedReader processOutputReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));) {
                    String readLine;
                    while (( readLine = processOutputReader.readLine()) != null){
                        log.info(readLine);
                    }
                    process.waitFor(5, TimeUnit.SECONDS);
                    if(process.isAlive())process.destroyForcibly();
                }
            }catch (Exception e){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean serviceCommand(String service, String cmd, String version, String success) {
        ProcessBuilder pb = new ProcessBuilder("service", service, cmd, version);
        pb.redirectErrorStream();
        Boolean result = false;
        try {
            Process process = pb.start();
            try (BufferedReader processOutputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while (( readLine = processOutputReader.readLine()) != null){
                    if(readLine.contains(success))result = true;
                }
                process.waitFor(5, TimeUnit.SECONDS);
                if(process.isAlive())process.destroyForcibly();
            }
        }catch (Exception e){
            log.warn("Service "+cmd+" on "+service+" failed "+e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public int getUptime(int pid) {
        ProcessBuilder pb = new ProcessBuilder("ps", "--pid", String.valueOf(pid), "-o etime");
        pb.redirectErrorStream();
        int result = 0;
        try {
            Process process = pb.start();
            try (BufferedReader processOutputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));) {
                String readLine;
                Pattern p = Pattern.compile("((\\d+)-)?\\d{2}:\\d{2}:\\d{2}");
                while (( readLine = processOutputReader.readLine()) != null){
                    Matcher m = p.matcher(readLine);
                    if(m.find()){
                        result = Integer.parseInt(m.group(5)) + 60*Integer.parseInt(m.group(4)) +
                                Integer.parseInt(m.group(3))*3600 +(m.group(2).isEmpty()?0:Integer.parseInt(m.group(2))*86400);
                    }
                }
            }
        }catch (Exception e){}
        return result;
    }

    @Override
    public void setAutoRun(String code, boolean autoRun) {
        ProcessBuilder pb = new ProcessBuilder("systemctl", autoRun?"enable":"disable", code);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            process.waitFor(5, TimeUnit.SECONDS);
            if(process.isAlive())process.destroyForcibly();
        }catch (IOException | InterruptedException e){
            log.warn("systemctl autorun setting failed" + e.getMessage());
        }
    }

    @Override
    public boolean isAutoRun(String code) {
        ProcessBuilder pb = new ProcessBuilder("systemctl", "status", code);
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader processOutputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while (( readLine = processOutputReader.readLine()) != null){
                    if(readLine.contains("enabled;"))return true;
                    if(readLine.contains("disabled;"))return false;
                }
            }
        }catch (Exception e){
            log.warn("systemctl failed to get service status "+e.getMessage());
        }
        return false;
    }
}
