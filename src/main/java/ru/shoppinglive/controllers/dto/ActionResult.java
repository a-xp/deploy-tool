package ru.shoppinglive.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by rkhabibullin on 21.09.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResult {
    private boolean success = true;
}
