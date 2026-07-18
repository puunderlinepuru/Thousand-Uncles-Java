package com.thousand_uncles.discord_bot.bot.listeners.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thousand_uncles.discord_bot.bot.service.BotActionsService;
import com.thousand_uncles.discord_bot.bot.service.RabbitQueuePurgeService;
import com.thousand_uncles.discord_bot.bot.util.AppNotifications;
import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.bot.util.MapSession;
import com.thousand_uncles.discord_bot.bot.util.MapSessionTracker;
import com.thousand_uncles.discord_bot.data.models.TestRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordServiceProd;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Component
public class RabbitEventListener {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    GlobalThings globalThings;

    @Autowired
    MapSessionTracker mapSessionTracker;

    private static final HashMap<String, String> mapsAndPings;
    static {
        mapsAndPings = new HashMap<>();
        mapsAndPings.put("cp_dustbowl", "<@229734102071246850>");
        mapsAndPings.put("ctf_turbine", "<@198878105736052737>");
        mapsAndPings.put("pl_aquarius", "<@1302298215885836318>");
    }

    private static final HashMap<String, String> serversAndIPs;
    static {
        serversAndIPs = new HashMap<>();
        serversAndIPs.put("0.0.0.0:27051", "R.U.N. Uncle Fight Club");
        serversAndIPs.put("0.0.0.0:27002", "MVM R.U.N. Uncle Fight Club");
        serversAndIPs.put("0.0.0.0:27133", "PUBLIC R.U.N 1ku (lowpop server)");
        serversAndIPs.put("0.0.0.0:27043", "Private R.U.N. 1v1KU 1");
        serversAndIPs.put("0.0.0.0:27035", "Private R.U.N. 1v1KU 2");
        serversAndIPs.put("0.0.0.0:27316", "Private R.U.N. 1v1KU 3");
    }
    private final Map<String, String> serversMaps = new ConcurrentHashMap<>();

    @Autowired
    BotActionsService botActionsService;


    RabbitEventListener(RabbitQueuePurgeService rabbitQueuePurgeService){
        rabbitQueuePurgeService.purgeQueue("event.queue");
        AppNotifications.RabbitMQ.RABBITMQ_CONSUME_INFO("purged");
    }

    @RabbitListener(queues = "event.queue")
    public void receiveMessage(String message) {
        JsonNode eventNode;

        try{
            eventNode = processJSON(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("JSON node: " + jsonNode.toPrettyString());
        String eventName = eventNode.get("event_name").asText();
        JsonNode eventData = eventNode.get("event_data");
        String serverName = serversAndIPs.get(eventNode.get("srv").asText());
        System.out.println(eventName + " on " + serverName);
        long tick;
        if (eventName.equals("player_spawn") || eventName.equals("player_connect")){
            return;
        }
        System.out.println("data: " + eventData.toPrettyString());

        switch (eventName){
            case "OnMapStart":
                String mapName = eventData.get("map").asText();
                isCertainMap(mapName, serverName);
                serversMaps.put(serverName, eventData.get("map").asText());
                AppNotifications.RUNserver.RUN_EVENT_INFO("Started map: " + mapName);
            case "teamplay_round_start":
                if (serversMaps.get(serverName) == null){
                    AppNotifications.RUNserver.RUN_EVENT_INFO("Map start event was missed on " + serverName + ". No current map is present");
                    break;
                }
                mapSessionTracker.handleMapStart(serversMaps.get(serverName), serverName);
                break;
            case "teamplay_setup_finished":
                tick = eventNode.get("tick").asLong();
                try{
                    mapSessionTracker.handleSetupFinished(tick, serverName);
                } catch (Exception e){
                    AppNotifications.RUNserver.RUN_EVENT_WARNING("got setup finish event with broken data");
                    break;
                }
                System.out.println("teamplay_setup_finished");
                System.out.println(eventNode.toPrettyString());
                break;
            case "teamplay_round_win":
                tick = eventNode.get("tick").asLong();
                if (eventData.get("team").asInt() != 3){
                    AppNotifications.RUNserver.RUN_EVENT_INFO("Ghosts are playing on " + serverName + " session dropped.");
                    return;
                }
                if (eventData.get("full_round").asText().equals("0")){
                    mapSessionTracker.handleNextStage(tick, serverName);
                } else if (eventData.get("full_round").asText().equals("1")) {
                    mapSessionTracker.handleRoundWin(tick, serverName);
                } else {
                    AppNotifications.RabbitMQ.RABBITMQ_CONSUME_ERROR("Couldn't get full_round from event_data in event");
                }
//                addTestRecord(eventNode);
                break;
//            case "player_changeclass":
//                TestRecord testRecord = mapRecordServiceProd.getTestRecord();
//                System.out.printf("%.20f%n", testRecord.getCurr_wr_seconds());
        }
    }

    private void addTestRecord(JsonNode eventNode){
        JsonNode eventData = eventNode.get("event_data");

        int ID = 1;
//        String mapName = eventData.get("map").asText();
        String mapName = "tofill";
        BigDecimal finishTimeString = BigDecimal.valueOf(eventData.get("round_time").asDouble());
        System.out.println("finishTimeString: ");
        System.out.printf("%.6f%n", finishTimeString);
        TestRecord savedRecord = mapRecordServiceProd.addTestRecord(
                ID,
                mapName,
                finishTimeString,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        BigDecimal savedTime = savedRecord.getCurr_wr_seconds();
        System.out.printf("%.20f%n", savedTime);
    }

    //data: {
//  "team" : 3,
//  "winreason" : 1,
//  "flagcaplimit" : 3,
//  "full_round" : 1,
//  "round_time" : 1625.08496,
//  "losing_team_num_caps" : 0,
//  "was_sudden_death" : 0
//}

    private void isCertainMap(String mapName, String serverName){
        switch (mapName){
            case "cp_dustbowl", "ctf_turbine", "pl_aquarius":
                mapAlert(mapName, serverName);
                break;
        }
    }

    private void mapAlert(String mapName, String serverName){ botActionsService.sendIntoCurrentlyGaming(mapsAndPings.get(mapName) + mapName + " alert on " + serverName + "!"); }

    private JsonNode processJSON(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(message);
    }
}

//          PLAYER JOIN FLOW
/*
    "OnClientPutInServer"
    data: {
      "client" : 43,
      "connected" : true,
      "in_game" : true,
      "userid" : 166,
      "bot" : false,
      "name" : "polskiszpieg",
      "auth" : "STEAM_0:0:388798986"
    }
    "player_initial_spawn"
    data: {
      "index" : 43
    }
    "player_spawn"
    data: {
      "userid" : 166,
      "team" : 0,
      "class" : 0
    }
    "player_activate"
    data: {
      "userid" : 166
    }
    */

//          MAP STARTING FLOW
/*
teamplay_round_active on R.U.N. Uncle Fight Club
data: { }
TF2_OnWaitingForPlayersEnd on R.U.N. Uncle Fight Club
data: { }
teamplay_round_selected on R.U.N. Uncle Fight Club
data: {
        "round" : "round_1"
        }
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
player_spawn on R.U.N. Uncle Fight Club
teamplay_round_start on R.U.N. Uncle Fight Club
data: {
        "full_reset" : true
        }
teamplay_round_active on R.U.N. Uncle Fight Club
data: { }
Event type: class discord4j.core.event.domain.message.MessageUpdateEvent
Event type: class discord4j.core.event.domain.message.MessageCreateEvent
teamplay_setup_finished on R.U.N. Uncle Fight Club
data: { }
*/

//          MAP ENDING FLOW
/*
*"OnMapEnd"
*data: {
*  "map" : "cp_dustbowl",
*  "next_map_set" : false
*}
*"OnMapInit"
*data: {
*  "map" : "cp_cargo"
*}
*"OnMapStart"
*data: {
*  "map" : "cp_cargo"
*}
*        "OnMapVoteStarted"
*data: {
*  "map" : "cp_dustbowl",
*  "next_map_set" : false
*}
*/

//          STAGE VITORY FLOW
/*
teamplay_point_captured
    data: {
      "cp" : 3,
      "cpname" : "#Dustbowl_cap_2_B",
      "team" : 3,
      "cappers" : "*,"
    }
    teamplay_round_win
    data: {
      "team" : 3,
      "winreason" : 1,
      "flagcaplimit" : 3,
      "full_round" : 0,
      "round_time" : 1813.529907,
      "losing_team_num_caps" : 0,
      "was_sudden_death" : 0
    }
    teamplay_timer_time_added
    data: {
      "timer" : 418,
      "seconds_added" : 0
    }
            teamplay_round_selected
    data: {
      "round" : "round_3"
    }
    */

//          VICTORY FLOW
/*
teamplay_point_startcapture on R.U.N. Uncle Fight Club
data: {
  "cp" : 2,
  "cpname" : "#Badwater_cap_4",
  "team" : 2,
  "capteam" : 3,
  "cappers" : "\u0001",
  "captime" : 265.079956
}
player_spawn on R.U.N. Uncle Fight Club
teamplay_point_captured on R.U.N. Uncle Fight Club
data: {
  "cp" : 2,
  "cpname" : "#Badwater_cap_4",
  "team" : 3,
  "cappers" : "\u0001"
}
teamplay_round_win on R.U.N. Uncle Fight Club
data: {
  "team" : 3,
  "winreason" : 1,
  "flagcaplimit" : 3,
  "full_round" : 1,
  "round_time" : 1625.08496,
  "losing_team_num_caps" : 0,
  "was_sudden_death" : 0
}
*/

//          PLAYER INIT
/*
OnClientAuthorized on R.U.N. Uncle Fight Club
data: {
  "client" : 42,
  "connected" : true,
  "in_game" : false,
  "userid" : 1115,
  "bot" : false,
  "name" : "Zumakc",
  "auth" : "STEAM_0:1:67554236"
}
OnClientAuthorized on R.U.N. Uncle Fight Club
data: {
  "client" : 43,
  "connected" : true,
  "in_game" : false,
  "userid" : 1116,
  "bot" : false,
  "name" : "pnbnnyb",
  "auth" : "STEAM_0:0:215842381"
}
OnClientAuthorized on R.U.N. Uncle Fight Club
data: {
  "client" : 1,
  "connected" : true,
  "in_game" : false,
  "userid" : 1034,
  "bot" : false,
  "name" : "John E. Calculus",
  "auth" : "STEAM_0:1:447807859"
}
OnClientPutInServer on R.U.N. Uncle Fight Club
data: {
  "client" : 1,
  "connected" : true,
  "in_game" : true,
  "userid" : 1034,
  "bot" : false,
  "name" : "John E. Calculus",
  "auth" : "76561198855881447"
}
player_initial_spawn on R.U.N. Uncle Fight Club
data: {
  "index" : 1
}
player_spawn on R.U.N. Uncle Fight Club
player_activate on R.U.N. Uncle Fight Club
data: {
  "userid" : 1034,
  "auth" : "76561198855881447"
  */

//          STAGE 1 W
/*
teamplay_round_win on R.U.N. Uncle Fight Club
data: {
  "team" : 3,
  "winreason" : 1,
  "flagcaplimit" : 3,
  "full_round" : 0,
  "round_time" : 387.299987,
  "losing_team_num_caps" : 0,
  "was_sudden_death" : 0
}

teamplay_point_captured on R.U.N. Uncle Fight Club
data: {
  "cp" : 2,
  "cpname" : "#cp_mojave_2a",
  "team" : 3,
  "cappers" : "\u0001"
}
*/

//          STAGE 2 W
/*
teamplay_point_captured on R.U.N. Uncle Fight Club
data: {
  "cp" : 3,
  "cpname" : "#cp_mojave_2b",
  "team" : 3,
  "cappers" : "\u0001*"
}
teamplay_round_win on R.U.N. Uncle Fight Club
data: {
  "team" : 3,
  "winreason" : 1,
  "flagcaplimit" : 3,
  "full_round" : 1,
  "round_time" : 374.399963,
  "losing_team_num_caps" : 0,
  "was_sudden_death" : 0
}
 W
finishTimeString:
374.399963
Hibernate: select tr1_0.id,tr1_0.curr_wr_seconds,tr1_0.map_name,tr1_0.prev_wr_seconds,tr1_0.proof_img_1_link,tr1_0.proof_img_2_link,tr1_0.proof_img_3_link,tr1_0.proof_vid_link,tr1_0.stage_1_time_seconds,tr1_0.stage_2_time_seconds,tr1_0.stage_3_time_seconds from test tr1_0 where tr1_0.id=?
Hibernate: update test set curr_wr_seconds=?,map_name=?,prev_wr_seconds=?,proof_img_1_link=?,proof_img_2_link=?,proof_img_3_link=?,proof_vid_link=?,stage_1_time_seconds=?,stage_2_time_seconds=?,stage_3_time_seconds=? where id=?
374.39996300000000000000
teamplay_timer_time_added on R.U.N. Uncle Fight Club
data: {
  "timer" : 286,
  "seconds_added" : 0
}
*/
