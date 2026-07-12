package com.thousand_uncles.discord_bot.bot.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thousand_uncles.discord_bot.bot.BotActions;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@SuppressWarnings("unused")
@Component
public class RabbitEventListener {

    private static final HashMap<String, String> mapsAndPings;
    static {
        mapsAndPings = new HashMap<>();
        mapsAndPings.put("cp_dustbowl", "<@229734102071246850>");
        mapsAndPings.put("ctf_turbine", "<@198878105736052737>");
    }

    String serverName = "R.U.N. Uncle Fight Club";

    @Autowired
    BotActions botActions;

    @Bean
    public DirectExchange eventExchange() {
        return new DirectExchange("tf2.round.completed");
    }

    @Bean
    public org.springframework.amqp.core.Queue eventQueue() {
        return new Queue("tf2.round.completed");
    }

    @SuppressWarnings("unused")
    @Bean
    public Binding eventBinding() {
        return BindingBuilder.bind(eventQueue()).to(eventExchange()).with("tf2.round.completed");
    }

    @RabbitListener(queues = "tf2.round.completed")
    public void receiveMessage(String message) {
//        System.out.println("Received message: " + message);

        JsonNode jsonNode;

        try{
            jsonNode = processJSON(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        System.out.println("JSON node: " + jsonNode.toPrettyString());
        String eventName = jsonNode.get("event_name").asText();
        JsonNode eventData = jsonNode.get("event_data");
        System.out.println(eventName);
        if (eventName.equals("player_spawn") || eventName.equals("player_connect")){
            return;
        }
        System.out.println("data: " + eventData.toPrettyString());

//        PLAYER JOIN FLOW
//        "OnClientPutInServer"
//data: {
//  "client" : 43,
//  "connected" : true,
//  "in_game" : true,
//  "userid" : 166,
//  "bot" : false,
//  "name" : "polskiszpieg",
//  "auth" : "STEAM_0:0:388798986"
//}
//"player_initial_spawn"
//data: {
//  "index" : 43
//}
//"player_spawn"
//data: {
//  "userid" : 166,
//  "team" : 0,
//  "class" : 0
//}
//"player_activate"
//data: {
//  "userid" : 166
//}


//        MAP ENDING FLOW

//        "OnMapEnd"
//data: {
//  "map" : "cp_dustbowl",
//  "next_map_set" : false
//}
//"OnMapInit"
//data: {
//  "map" : "cp_cargo"
//}
//"OnMapStart"
//data: {
//  "map" : "cp_cargo"
//}
//        "OnMapVoteStarted"
//data: {
//  "map" : "cp_dustbowl",
//  "next_map_set" : false
//}

//        VICTORY FLOW
//        teamplay_point_captured
//data: {
//  "cp" : 3,
//  "cpname" : "#Dustbowl_cap_2_B",
//  "team" : 3,
//  "cappers" : "*,"
//}
//teamplay_round_win
//data: {
//  "team" : 3,
//  "winreason" : 1,
//  "flagcaplimit" : 3,
//  "full_round" : 0,
//  "round_time" : 1813.529907,
//  "losing_team_num_caps" : 0,
//  "was_sudden_death" : 0
//}
//teamplay_timer_time_added
//data: {
//  "timer" : 418,
//  "seconds_added" : 0
//}
//        teamplay_round_selected
//data: {
//  "round" : "round_3"
//}

        switch (eventName){
            case "OnMapStart":
                isCertainMap(eventData);
                break;
        }
//        botActions.sendIntoCave(jsonNode.toPrettyString());
    }

    private void isCertainMap(JsonNode event_data){
        String mapName = event_data.get("map").asText();
        if (mapName.equals("cp_dustbowl")){botActions.sendIntoCave(mapsAndPings.get(mapName) + " Dustbowl alert on " + serverName + "!");}
        if (mapName.equals("ctf_turbine")){botActions.sendIntoCave(mapsAndPings.get(mapName) + " Turbine alert on " + serverName + "!");}
    }

    private JsonNode processJSON(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(message);
    }
}
