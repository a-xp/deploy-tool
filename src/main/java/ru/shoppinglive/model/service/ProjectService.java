package ru.shoppinglive.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.shoppinglive.model.entity.filesystem.ScriptMeta;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.project.ProjectParams;
import ru.shoppinglive.model.repository.ProjectRepository;
import ru.shoppinglive.model.service.local.OsService;
import ru.shoppinglive.model.service.local.ScriptService;

import java.util.Collection;

/**
 * Created by rkhabibullin on 03.07.2017.
 */
@Component
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private Environment environment;
    @Autowired
    private ScriptService scriptService;
    @Autowired
    private OsService osService;

    public Collection<Project> getAll(){
        return projectRepository.findAll();
    }

    public Project find(String code){
        return projectRepository.findFirstByCode(code);
    }

    public Project getById(int id){ return projectRepository.getOne(id);}

    public void saveParams(int id, ProjectParams params){
        Project project = projectRepository.getOne(id);
        ScriptMeta meta = new ScriptMeta(project.getCode(), params.getDefaultVersion(), params.getMem()>0?params.getMem()+"m":"",
                environment.getActiveProfiles()[0], "");
        scriptService.createScript(project.getType(), meta);
        osService.setAutoRun(project.getCode(), params.isAutoRun());
        project.setAutoReload(params.isAutoReload());
        projectRepository.save(project);
    }

    public ProjectParams getParams(int id) {
        Project project = projectRepository.getOne(id);
        ScriptMeta script = scriptService.getScript(project.getCode(), project.getType());
        if(script!=null){
            return new ProjectParams(true, Integer.parseInt(script.getMemory().replaceAll("\\D", "")),
                    script.getAdditionalArgs(), script.getDefaultVersion(), osService.isAutoRun(project.getCode()),
                    project.isAutoReload());
        }else{
            return new ProjectParams();
        }
    }

}
