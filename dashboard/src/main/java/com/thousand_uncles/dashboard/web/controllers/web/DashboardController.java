package com.thousand_uncles.dashboard.web.controllers.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@SuppressWarnings("unused")
public class DashboardController {

    @GetMapping("/")
    public void Thing(){
        System.out.println("user here");
    }
}
