package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shoppinglive.controllers.dto.ActionResult;
import ru.shoppinglive.controllers.dto.RunRequest;
import ru.shoppinglive.model.entity.gitlab.Job;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.project.ProjectParams;
import ru.shoppinglive.model.service.ProjectService;
import ru.shoppinglive.model.service.local.InstanceService;
import ru.shoppinglive.model.service.local.OsService;
import ru.shoppinglive.model.service.remote.GitlabService;

import javax.validation.Valid;

/**
 * Created by rkhabibullin on 21.09.2017.
 */
@RestController
public class InstanceController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private GitlabService gitlabService;
    @Autowired
    private OsService osService;
    @Autowired
    private InstanceService instanceService;

    @PostMapping("/api/projects/{id}/run")
    public ActionResult runJar(@RequestBody @Valid RunRequest request, @PathVariable("id") int id){
        Project project = projectService.getById(id);
        ProjectParams params = projectService.getParams(id);
        if(params.isCreated()){
            Job job = gitlabService.getPipelineJobs(project.getGitlabId(), request.getPipelineId()).get(0);
            if(job!=null){
                return new ActionResult(gitlabService.getVersion(project.getGitlabId(), job.getId()).map(
                    logData -> {
                        if(!osService.getDownloadedVersions(project.getCode()).contains(logData.getVersion())){
                            osService.downloadJar(project.getCode(), logData.getVersion(), logData.getJarPath());
                        }
                        return osService.run(project.getCode(), logData.getVersion());
                    }
                ).orElse(false));
            }
        }
        return new ActionResult(false);
    }

    @PostMapping("/api/projects/{id}/instances/{pid}/stop")
    public ActionResult stopInstance(@PathVariable("id") int id, @PathVariable("pid") int pid){
        Project project = projectService.getById(id);
        return new ActionResult(instanceService.killInstance(pid));
    }

}
