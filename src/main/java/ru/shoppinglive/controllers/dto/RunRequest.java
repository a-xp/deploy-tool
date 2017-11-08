package ru.shoppinglive.controllers.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by rkhabibullin on 21.09.2017.
 */
@Data
public class RunRequest {
    @NotEmpty
    private String version;
    private String ip;
}
