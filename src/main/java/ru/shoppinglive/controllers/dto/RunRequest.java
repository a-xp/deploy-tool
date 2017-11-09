package ru.shoppinglive.controllers.dto;

import lombok.Data;

/**
 * Created by rkhabibullin on 21.09.2017.
 */
@Data
public class RunRequest {
    private int pipelineId;
    private String ip;
}
