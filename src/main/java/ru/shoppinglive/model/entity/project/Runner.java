package ru.shoppinglive.model.entity.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rkhabibullin on 15.09.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Runner {
    public String ip;
    public String hostName;
}
