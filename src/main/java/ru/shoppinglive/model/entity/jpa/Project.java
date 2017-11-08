package ru.shoppinglive.model.entity.jpa;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by rkhabibullin on 03.07.2017.
 */

@Entity
@Table(name = "soa_project")
@Data
public class Project {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String code;
    private Date lastActivity;
    @Enumerated(EnumType.STRING)
    private Type type;
    private int gitlabId;
    private boolean autoReload;

    public enum Type{
        cron, service
    }
}
