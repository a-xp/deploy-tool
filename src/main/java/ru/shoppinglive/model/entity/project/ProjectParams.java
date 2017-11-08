package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by rkhabibullin on 08.11.2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProjectParams {
    private int mem;
    private String additionalParams;
    private String defaultVersion;
    private boolean autoRun;
    private boolean autoReload;
}
