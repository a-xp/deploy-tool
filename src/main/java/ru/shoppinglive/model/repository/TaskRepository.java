package ru.shoppinglive.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shoppinglive.model.entity.jpa.Task;


/**
 * Created by rkhabibullin on 13.09.2017.
 */
public interface TaskRepository extends JpaRepository<Task,Integer>{
}
