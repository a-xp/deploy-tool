package ru.shoppinglive.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.shoppinglive.model.entity.jpa.Project;
import ru.shoppinglive.model.repository.ProjectRepository;

import java.util.Collection;

/**
 * Created by rkhabibullin on 03.07.2017.
 */
@Component
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Collection<Project> getAll(){
        return projectRepository.findAll();
    }


}
