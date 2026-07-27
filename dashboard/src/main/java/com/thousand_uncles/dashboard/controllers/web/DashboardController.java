package com.thousand_uncles.dashboard.controllers.web;

import com.thousand_uncles.data.models.MapRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class DashboardController {

    private ArrayList<MapRecord> mapRecordArrayList = new ArrayList<>();

    public DashboardController(){
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "Hello World from Thymeleaf!");
        return "hello";
    }

    @GetMapping("/maps")
    public String maps(Model model) {
        model.addAttribute("maps", mapRecordArrayList);
        return "maps_page";
    }

    @GetMapping("/hud")
    public String hud(Model model) {
        return "hud";
    }
}

