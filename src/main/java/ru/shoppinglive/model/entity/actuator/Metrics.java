package ru.shoppinglive.model.entity.actuator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Metrics {
    private Long uptime;
    private Long mem;
    @JsonProperty("mem.free")
    private Long memFree;
}
