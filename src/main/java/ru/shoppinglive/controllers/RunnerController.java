package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shoppinglive.model.entity.project.Runner;
import ru.shoppinglive.model.service.remote.RunnerService;

import java.util.Collection;

/**
 * Created by rkhabibullin on 20.09.2017.
 */
@RestController
@RequestMapping("/api/runners")
public class RunnerController {

    @Autowired
    private RunnerService runnerService;

    @GetMapping
    public Collection<Runner> list(){
        return runnerService.getRunners();
    }

}
