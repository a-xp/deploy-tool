package ru.shoppinglive.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shoppinglive.controllers.dto.ActionResult;
import ru.shoppinglive.controllers.dto.RunRequest;

import javax.validation.Valid;

/**
 * Created by rkhabibullin on 21.09.2017.
 */
@RestController
public class InstanceController {

    @PostMapping
    public ActionResult runJar(@RequestBody @Valid RunRequest request){



        return new ActionResult();
    }


}
