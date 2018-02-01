package ru.shoppinglive.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * Created by RKhabibullin on 01.02.2018.
 */
@Controller
public class IndexController {

    @RequestMapping({"/", "/app/projects/**", "/app/users/**", "/app/config/**", "/app/status/**", "/app/profile/**", "/app/login"})
    public View index(ModelMap model){
        return new InternalResourceView("/app/index.html");
    }

}
