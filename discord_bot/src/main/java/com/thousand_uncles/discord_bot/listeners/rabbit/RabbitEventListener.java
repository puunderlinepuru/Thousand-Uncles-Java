package com.thousand_uncles.discord_bot.listeners.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.data.models.TestRecord;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.service.BotActionsService;
import com.thousand_uncles.discord_bot.service.RabbitActionsService;
import com.thousand_uncles.discord_bot.service.RabbitQueuePurgeService;
import com.thousand_uncles.discord_bot.util.AppNotifications;
import com.thousand_uncles.discord_bot.util.DiscordBotResponseFormatter;
import com.thousand_uncles.discord_bot.util.GlobalThings;
import com.thousand_uncles.discord_bot.util.MapSessionTracker;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.thousand_uncles.discord_bot.util.JSONHandler.processJSON;

@SuppressWarnings("unused")
@Component
public class RabbitEventListener {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    GlobalThings globalThings;

    @Autowired
    MapSessionTracker mapSessionTracker;

    RabbitActionsService rabbitActionsService;

    private static final Map<String, String> verdicts = new ConcurrentHashMap<>();

    public static void setVerdict(String serverName, String message){
        String serverID = serverIDs.get(serverName);
        verdicts.put(serverID, message);
    }

    private static final HashMap<String, String> mapsAndPings;
    static {
        mapsAndPings = new HashMap<>();
        mapsAndPings.put("cp_dustbowl", "<@229734102071246850>");
        mapsAndPings.put("ctf_turbine", "<@198878105736052737>");
        mapsAndPings.put("pl_aquarius", "<@1302298215885836318>");
    }

    private static final Map<String, String> serverIDs;
    static {
        serverIDs = new HashMap<>();
        serverIDs.put("R.U.N. Uncle Fight Club", "server1");
        serverIDs.put("MVM R.U.N. Uncle Fight Club", "server2");
        serverIDs.put("PUBLIC R.U.N 1ku (lowpop server)", "server3");
        serverIDs.put("Private R.U.N. 1v1KU 1", "server4");
        serverIDs.put("Private R.U.N. 1v1KU 2", "server5");
        serverIDs.put("Private R.U.N. 1v1KU 3", "server6");
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

    RabbitEventListener(RabbitQueuePurgeService rabbitQueuePurgeService, RabbitActionsService rabbitActionsService){
        rabbitQueuePurgeService.purgeQueue("event.queue");
        AppNotifications.RabbitMQ.RABBITMQ_CONSUME_INFO("purged");
        this.rabbitActionsService = rabbitActionsService;
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
        ObjectNode eventData = (ObjectNode) eventNode.get("event_data");
        String serverName = serversAndIPs.get(eventNode.get("srv").asText());
        System.out.println(eventName + " on " + serverName);
        long tick;
        try{
            tick = eventNode.get("tick").asLong();
        } catch (Exception e) {
            AppNotifications.RabbitMQ.RABBITMQ_CONSUME_ERROR("'tick' field doesn't exist.");
            return;
        }
//        System.out.println("data: " + eventData.toPrettyString());

        switch (eventName){
            case "OnMapStart":
                onMapStartHandle(serverName, eventData);
                break;
            case "teamplay_round_active":
                teamplayRoundActiveHandle(serverName, tick);
                break;
            case "teamplay_setup_finished":
                teamplaySetupFinishedHandle(serverName, tick);
                break;
            case "teamplay_round_win":
                teamplayRoundWinHandle(serverName, tick, eventData);
                break;
            case "game_end":
                gameEndHandle();
                break;
        }
    }

    private void onMapStartHandle(String serverName, JsonNode eventData){
        String mapName = eventData.get("map").asText();
        isCertainMap(mapName, serverName);
        serversMaps.put(serverName, mapName);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Started map: " + mapName);
    }

    private void teamplayRoundActiveHandle(String serverName, long tick){
        String chatMessage = chatMapRecordMessage(serverName);
        String serverID = serverIDs.get(serverName);
        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
        rabbitActionsService.sendToCommand(serverID, "PrintCenterTextAll", verdicts.get(serverID));
        verdicts.remove(serverID);
        if (serversMaps.get(serverName) == null){
            AppNotifications.RUNserver.RUN_EVENT_INFO("Map start event was missed on " + serverName + ". No current map is present");
            return;
        }
        try{
            mapSessionTracker.handleStageStart(serversMaps.get(serverName), tick, false, serverName);
        } catch (Exception e){
            AppNotifications.RUNserver.RUN_EVENT_WARNING("got round_active event with broken data");
        }
    }

    private void teamplaySetupFinishedHandle(String serverName, long tick){
        if (serversMaps.get(serverName) == null){
            AppNotifications.RUNserver.RUN_EVENT_INFO("Map start event was missed on " + serverName + ". No current map is present");
            return;
        }
        try{
            mapSessionTracker.handleStageStart(serversMaps.get(serverName), tick, true, serverName);
        } catch (Exception e){
            AppNotifications.RUNserver.RUN_EVENT_WARNING("got setup_finished event with broken data");
        }
    }

    private void teamplayRoundWinHandle(String serverName, long tick, ObjectNode eventData){
        if (eventData.get("team").asInt() != 3){
            AppNotifications.RUNserver.RUN_EVENT_INFO("Ghosts are playing on " + serverName + " session dropped.");
            return;
        }
        boolean isFullRound;
        try{
            isFullRound = eventData.get("full_round").asText().equals("1");
        } catch (Exception e){
            AppNotifications.RabbitMQ.RABBITMQ_CONSUME_ERROR("Couldn't get full_round from event_data in event");
            return;
        }
        if (isFullRound){
            mapSessionTracker.handleNextStage(tick, serverName);
        } else{
            mapSessionTracker.handleRoundWin(tick, serverName);
        }
    }

    private void gameEndHandle(){

    }

    private void isCertainMap(String mapName, String serverName){
        switch (mapName){
            case "cp_dustbowl", "ctf_turbine", "pl_aquarius":
                mapAlert(mapName, serverName);
                break;
        }
    }

    private String chatMapRecordMessage(String serverName){
        String mapUnsuffixed = GlobalThings.getMapSuffixes().get(serversMaps.get(serverName));
        int mapID;
        if (mapUnsuffixed == null){
            mapID = GlobalThings.getMapIDS().indexOf(serversMaps.get(serverName));
        } else {
            mapID = GlobalThings.getMapIDS().indexOf(mapUnsuffixed);
        }
        String chatMessage;
        if (mapID == -1){
            chatMessage = "map doesn't exist in maplist. Can't get record";
            return chatMessage;
        }
        TestRecord existingRecord = mapRecordServiceProd.getTestRecord(mapID);
        if (existingRecord == null) {
            chatMessage = "Couldn't find existing record for map. You may be the first!";
            return chatMessage;
        }
        String currWRtime = DiscordBotResponseFormatter.NumberToString(existingRecord.getCurr_wr_seconds());
        chatMessage = "Current WR for map: " + currWRtime;
        return chatMessage;
    }

    private void mapAlert(String mapName, String serverName){ botActionsService.sendIntoCurrentlyGaming(mapsAndPings.get(mapName) + mapName + " alert on " + serverName + "!"); }
}


