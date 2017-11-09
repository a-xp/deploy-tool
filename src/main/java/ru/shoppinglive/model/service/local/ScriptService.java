package ru.shoppinglive.model.service.local;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.shoppinglive.Application;
import ru.shoppinglive.model.entity.filesystem.ScriptMeta;
import ru.shoppinglive.model.entity.jpa.Project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rkhabibullin on 14.09.2017.
 */
@Component
@ConfigurationProperties(prefix = "deploy.script")
public class ScriptService {
    @Setter
    private String service;
    @Setter
    private String cron;
    @Autowired
    private Configuration freemarker;

    public ScriptMeta getScript(String code, Project.Type type){
        return parseParams(type==Project.Type.service?Paths.get(service, code):Paths.get(cron, code));
    }

    private ScriptMeta parseParams(Path file){
        if(Files.exists(file)){
            ScriptMeta result = new ScriptMeta();
            try {
                Pattern pattern = Pattern.compile("^([A-Z_]+)=\"(.*?)\"");
                Files.lines(file).forEachOrdered(l->{
                    Matcher matcher = pattern.matcher(l);
                    if(matcher.find()){
                        switch (matcher.group(1)){
                            case "MEM": result.setMemory(matcher.group(2)); break;
                            case "ENV": result.setEnv(matcher.group(2)); break;
                            case "SERVICE_NAME": result.setCode(matcher.group(2)); break;
                            case "DEFAULT_VERSION": result.setDefaultVersion(matcher.group(2)); break;
                        }
                    }
                });
                return result;
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            return null;
        }
    }

    public boolean createScript(Project.Type type, ScriptMeta meta){
        Path file = Paths.get(type==Project.Type.service?service:cron, meta.getCode());
        try {
            if(!Files.exists(file.getParent()))Files.createDirectories(file.getParent());
            Template template = freemarker.getTemplate(type+".ftlx");
            try(BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)){
                template.process(meta, bw);
            }
            return true;
        }catch (IOException | TemplateException e){
            Application.logger.warn("Failed to create "+type+" script for "+meta.getCode(), e);
            return false;
        }
    }

}
