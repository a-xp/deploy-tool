package ru.shoppinglive.model.entity.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by rkhabibullin on 12.09.2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pipeline {
    private int id;
    @JsonProperty("finished_at")
    private Date date;
}
