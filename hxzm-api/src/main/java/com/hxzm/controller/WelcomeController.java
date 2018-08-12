package com.hxzm.controller;

import com.hxzm.service.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
public class WelcomeController {

    @Autowired
    WelcomeService welcomeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getRootContent(){
        welcomeService.service1();
        welcomeService.service2();
        welcomeService.service3();
        welcomeService.service4();
        return "Welcome to fcjs-api!";
    }
}
