package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by rkhabibullin on 08.11.2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectParams {
    private boolean created;
    private int mem;
    private String additionalArgs;
    private String defaultVersion;
    private boolean autoRun;
    private boolean autoReload;
}
