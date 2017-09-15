package ru.shoppinglive.model.entity.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by rkhabibullin on 14.09.2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    private int id;
    private User user;
    private String name;
    @JsonProperty("finished_at")
    private Date finishedAt;
    private Commit commit;
}
