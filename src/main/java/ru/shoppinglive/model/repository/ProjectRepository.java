package ru.shoppinglive.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shoppinglive.model.entity.jpa.Project;

/**
 * Created by rkhabibullin on 03.07.2017.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findFirstByCode(String code);
}
