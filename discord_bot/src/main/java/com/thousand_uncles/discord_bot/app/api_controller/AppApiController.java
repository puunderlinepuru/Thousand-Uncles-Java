package com.thousand_uncles.discord_bot.app.api_controller;

import com.thousand_uncles.data.models.common.ManualIndexedMapRecordEntry;
import com.thousand_uncles.data.models.run.TestRecord;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.app.services.MapSessionTrackingService;
import com.thousand_uncles.discord_bot.common.dto.GamerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/app")
public class AppApiController {

    @Autowired
    MapSessionTrackingService mapSessionTrackingService;

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @PostMapping("/set/{serverName}")
    public String startGameSession(@PathVariable String serverName, @RequestBody String mapName){
//        botActionsService.sendIntoCave(message);
        System.out.println("got API call to start map: " + mapName);
        mapSessionTrackingService.handleMapStart(mapName, serverName);
        return "set, I think";
    }

    @PostMapping("/join/{serverName}")
    public String joinPlayer(@PathVariable String serverName, @RequestBody GamerDTO gamer){
        String playerAuthID = gamer.getPlayer_auth();
        int clientID = gamer.getClientID();
        System.out.println("got API call to add player: " + playerAuthID + "with ID " + clientID + " on server " + serverName);
        mapSessionTrackingService.handlePlayerJoin(serverName, playerAuthID, clientID);
        return "hi";
    }

    @PostMapping("add/test")
    public String addTestRecord(){

        TestRecord testEntry = new TestRecord(
                0,
                "name",
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(2.22),
                "link",
                "link",
                "link",
                "link",
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        ManualIndexedMapRecordEntry savedRecord1 = mapRecordServiceProd.saveUncletopiaAny(
                0,
                "name",
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(2.22),
                "link",
                "link",
                "link",
                "link",
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );

        TestRecord savedRecord = mapRecordServiceProd.addTestRecord(
                0,
                "name",
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(2.22),
                "link",
                "link",
                "link",
                "link",
                BigDecimal.ONE,
                BigDecimal.ONE,
                BigDecimal.ONE
        );
        return savedRecord1.getMap_name();
    }
}
