package ru.shoppinglive.model.entity.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by rkhabibullin on 14.09.2017.
 */
@Data
@EqualsAndHashCode(of="id")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Commit {
    private String title;
    private String id;
}
