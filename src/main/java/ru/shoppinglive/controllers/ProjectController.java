package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.project.Instance;
import ru.shoppinglive.model.service.ProjectService;
import ru.shoppinglive.model.service.TaskService;
import ru.shoppinglive.model.service.remote.RedmineService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rkhabibullin on 03.07.2017.
 */
@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/")
    public Collection<Project> getAll(){
        return projectService.getAll();
    }

    public Collection<Instance> instances(@PathVariable("code") String code){
        return Collections.emptyList();
    }

    @GetMapping("/qa-builds")
    public List<String> getQaBuilds(){
        return Collections.singletonList(taskService.getName(5097));
    }
}
