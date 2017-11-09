package ru.shoppinglive.model.entity.gitlab;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by rkhabibullin on 09.11.2017.
 */
@Data
@AllArgsConstructor
public class LogData {
    private String version;
    private String jarPath;
}
