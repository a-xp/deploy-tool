package ru.shoppinglive.model.entity.script;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rkhabibullin on 14.09.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScriptMeta {
    private String code = "";
    private String defaultVersion = "";
    private String memory = "";
    private String env = "";
    private String additionalArgs = "";
}
