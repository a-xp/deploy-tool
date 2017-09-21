package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.entity.project.Build;
import ru.shoppinglive.model.entity.project.Instance;
import ru.shoppinglive.model.service.BuildService;
import ru.shoppinglive.model.service.ProjectService;
import ru.shoppinglive.model.service.TaskService;

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
    private BuildService buildService;

    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public Collection<Project> getAll(){
        return projectService.getAll();
    }

    public Collection<Instance> instances(@PathVariable("code") String code){
        return Collections.emptyList();
    }

    @GetMapping("/{id}/builds")
    public List<Build> getBuilds(@PathVariable("id") int id){
        return buildService.getMasterBuilds(id);
    }

    @GetMapping("/{code}")
    public Project getOne(@PathVariable("code") String code){
        return projectService.find(code);
    }

    @GetMapping("/{id}/qa-builds")
    public List<Build> getQaBuilds(@PathVariable("id") int id) { return buildService.getQABuilds(id); }

    @GetMapping("/{id}/instances")
    public List<Instance> getInstances(@PathVariable("id") int id){
        return Collections.emptyList();
    }

}
