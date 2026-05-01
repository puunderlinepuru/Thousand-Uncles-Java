package com.thousand_uncles.dashboard.web.controllers.web;

import com.thousand_uncles.dashboard.data.service.MapRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@SuppressWarnings("unused")
public class DashboardController {

    @Autowired
    private MapRecordService mapRecordService;

    @GetMapping("/maps")
    public String showUserList(Model model) {
        model.addAttribute("maps", mapRecordService.getAllRecords());
        return "maps_page";
    }
}
