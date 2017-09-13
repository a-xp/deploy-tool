package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shoppinglive.model.entity.actuator.Metrics;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instance {
    public String version;
    public int pid;
    public int port;
    public Metrics actuator;
}
