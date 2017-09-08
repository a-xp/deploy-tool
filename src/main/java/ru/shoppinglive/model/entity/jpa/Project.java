package ru.shoppinglive.model.entity.jpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by rkhabibullin on 03.07.2017.
 */

@Entity
@Table(name = "soa_project")
@Data
public class Project {
    @Id
    @GeneratedValue
    public Integer id;
    public String name;
    public String code;
    public Date lastActivity;
    @Enumerated(EnumType.STRING)
    public Type type;

    public enum Type{
        cron, service
    }
}
