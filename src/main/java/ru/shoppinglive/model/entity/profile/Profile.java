package ru.shoppinglive.model.entity.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@Data
@AllArgsConstructor
public class Profile {
    private String login;
    private Set<String> rights;
}
