package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instance {
    public String version;
    public String server;
    public int pid;
    public int port;
    public int uptime;
}
