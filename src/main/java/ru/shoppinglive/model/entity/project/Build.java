package ru.shoppinglive.model.entity.project;

import lombok.Data;

import java.util.Date;

/**
 * Created by rkhabibullin on 12.09.2017.
 */
@Data
public class Build {
    private String version;
    private int pipelineId;
    private String env;
    private Date date;
    private String author;
    private String message;
}
