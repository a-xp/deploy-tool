package ru.shoppinglive.model.service.local;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by rkhabibullin on 27.03.2017.
 */
public abstract class OsService {
    @Value("${SL_ENV}")
    protected String env;
    @Value("${deploy.soa-dir}")
    protected FileSystemResource soaRoot;
    @Value("${deploy.maven.url}")
    protected String maven;

    public abstract boolean run(String name, String version);

    public abstract int getPortByPid(int pid);

    public abstract boolean killProcess(int pid);

    public boolean stopService(String code, String version){
        return false;
    };

    public boolean restartService(String code, String version) {return false; };

    public Boolean removeJar(String name, String version){
        Path jarFile = Paths.get(soaRoot.getPath(), "jar", name, name+"-"+version+".jar");
        if(Files.exists(jarFile)){
            try {
                Files.delete(jarFile);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public List<String> getDownloadedVersions(String name) {
        Path jarDir = Paths.get(soaRoot.getPath(), "jar", name);
        if(Files.exists(jarDir)){
            Pattern p = Pattern.compile("^"+name+"-([0-9\\.\\-]+)\\.jar$");
            try {
                return Files.list(jarDir).map(Path::getFileName).map(Path::toString).map(
                        filename -> {
                            Matcher m = p.matcher(filename);
                            return m.find() ? m.group(1) : null;
                        }
                ).filter(Objects::nonNull).collect(Collectors.toList());
            }catch (Exception e){}
        }
        return Collections.emptyList();
    }

    public Boolean downloadJar(String name, String version){
        Path logDir = Paths.get(soaRoot.getPath(), "logs");
        Path jarDir = Paths.get(soaRoot.getPath(), "jar", name);
        if(!Files.exists(logDir)){
            try {
                Files.createDirectories(logDir);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        if(!Files.exists(jarDir)){
            try {
                Files.createDirectories(jarDir);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
        Path jarFile = Paths.get(jarDir.toString(), name+"-"+version+".jar");
        if(!Files.exists(jarFile)) {
            try {
                URL jarSrc = new URL(maven + name + "/" + version + "/" + name + "-" + version + ".jar");
                try (InputStream is = jarSrc.openStream()) {
                    Files.copy(is, jarFile, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public abstract boolean runCron(String code, String version, String params);

    public Resource getLog(String code, String version){
        Path log = Paths.get(soaRoot.getPath(), "logs", code+version+".log");
        if(Files.exists(log)){
            return new FileSystemResource(log.toFile());
        }else{
            return null;
        }
    }

    public Set<String> getLoggedVersions(String code){
        Path logDir = Paths.get(soaRoot.getPath(), "logs");
        Pattern p = Pattern.compile("^"+code+"([0-9\\.\\-]+)\\.log$");
        try {
            return Files.list(logDir).map(Path::getFileName).map(Path::toString).map(
                    filename -> {
                        Matcher m = p.matcher(filename);
                        return m.find() ? m.group(1) : null;
                    }).filter(Objects::nonNull).collect(Collectors.toSet());
        }catch (IOException e){
            return Collections.emptySet();
        }
    }

}
