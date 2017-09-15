package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shoppinglive.model.entity.jpa.Task;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rkhabibullin on 12.09.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Build {
    private String version;
    private int pipelineId;
    private String env;
    private Date date;
    private String author;
    private String message;
    private List<Task> features;
    private Set<Flag> flags;

    public enum Flag {
        HAS_LOG, BUILDING, HAS_JAR, AUTOSTART, RUNNING, MERGING, STARTING, LOADING, INVALID
    }

    public void addFlag(Flag flag){
        if(flags==null)flags = new HashSet<>(1);
        flags.add(flag);
    }
}
