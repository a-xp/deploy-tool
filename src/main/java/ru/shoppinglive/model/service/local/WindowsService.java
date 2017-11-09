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

/**
 * Created by rkhabibullin on 27.03.2017.
 */
@Service
@Profile("local")
public class WindowsService extends OsService {
    private static final Logger log = LoggerFactory.getLogger("application");

    @Override
    public int getUptime(int pid) {
        return 0;
    }

    @Override
    public boolean run(String name, String version){
        Path logFile = Paths.get(soaRoot.getPath(), "logs", name+".log");
        Path jarDir = Paths.get(soaRoot.getPath(), "jar", name);
        Path jarFile = Paths.get(soaRoot.getPath(), "jar", name, name+"-"+version+".jar");
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "start", "\"service\"", "/B", "java", "-DSL_ENV="+env, "-Xmx200m", "-jar", jarFile.toString());
        pb.directory(jarDir.toFile());
        pb.redirectError(logFile.toFile());
        pb.redirectOutput(logFile.toFile());
        try {
            pb.start();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public int getPortByPid(int pid) {
        ProcessBuilder pb = new ProcessBuilder("netstat", "-nao", "-p", "TCP");
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            try (BufferedReader processOutputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));) {
                String readLine;
                while (( readLine = processOutputReader.readLine()) != null){
                    String[] parts = readLine.split("\\s+");
                    if(parts.length>=6 && parts[5].equals(String.valueOf(pid)) && parts[4].equals("LISTENING")){
                        String[] addrPart = parts[2].split(":");
                        return Integer.parseInt(addrPart[1]);
                    }
                }
                process.waitFor();
            }
        }catch (IOException | InterruptedException e){}
        return 0;
    }

    @Override
    public boolean killProcess(int pid) {
        ProcessBuilder pb = new ProcessBuilder("taskkill", "/pid", String.valueOf(pid), "/f");
        try {
            pb.start();
            return true;
        }catch (Exception e){};
        return false;
    }

    @Override
    public boolean runCron(String code, String version, String params) {
        Path shFile = Paths.get(soaRoot.getPath(), "cron", code+".bat");
        if(Files.exists(shFile)){
            ProcessBuilder pb = new ProcessBuilder(shFile.toString(), version, params);
            try {
                Process process = pb.start();

                try (BufferedReader processOutputReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));) {
                    String readLine;
                    while (( readLine = processOutputReader.readLine()) != null){
                        log.info(readLine);
                    }
                    process.waitFor();
                }

            }catch (IOException | InterruptedException  e){
                return false;
            }
            return true;
        }
        return false;
    }
}
