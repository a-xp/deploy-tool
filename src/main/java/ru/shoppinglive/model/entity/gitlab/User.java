package ru.shoppinglive.model.entity.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by rkhabibullin on 14.09.2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String name;
}
