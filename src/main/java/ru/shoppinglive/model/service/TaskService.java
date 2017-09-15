package ru.shoppinglive.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import ru.shoppinglive.model.entity.jpa.Task;
import ru.shoppinglive.model.repository.TaskRepository;
import ru.shoppinglive.model.service.remote.RedmineService;

/**
 * Created by rkhabibullin on 13.09.2017.
 */
@Component
public class TaskService {

    @Autowired
    private RedmineService redmineService;
    @Autowired
    private TaskRepository taskRepository;

    @Cacheable("features")
    public Task find(int id){
        Task task = taskRepository.findOne(id);
        if(task==null){
            try {
                String name = redmineService.getFeatureName(id);
                task = new Task(id, name);
                taskRepository.save(task);
                return task;
            }catch (Exception e){
                return null;
            }
        }else {
            return task;
        }
    }

}
