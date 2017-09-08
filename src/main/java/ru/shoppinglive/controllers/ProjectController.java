package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.service.ProjectService;

import java.util.Collection;

/**
 * Created by rkhabibullin on 03.07.2017.
 */
@RestController
@CrossOrigin
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public Collection<Project> getAll(){
        return projectService.getAll();
    }

}
