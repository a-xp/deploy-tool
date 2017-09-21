package ru.shoppinglive.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shoppinglive.model.entity.profile.Profile;
import ru.shoppinglive.model.service.jwt.AuthService;

import java.security.Principal;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@RestController
@RequestMapping("/profile")
public class ProfileController  {

    @Autowired
    private AuthService authService;

    @GetMapping("/current")
    public Profile getProfile(Principal principal){
        return  authService.getUserInfo(principal.getName());
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public String other(RuntimeException exception){
        return "BAD";
    }
}
