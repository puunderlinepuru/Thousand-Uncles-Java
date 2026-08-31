package com.thousand_uncles.discord_bot.app.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thousand_uncles.data.models.run.TestRecord;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.bot.services.BotActionsService;
import com.thousand_uncles.discord_bot.app.services.RabbitActionsService;
import com.thousand_uncles.discord_bot.app.services.RabbitQueuePurgeService;
import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import com.thousand_uncles.discord_bot.bot.util.DiscordBotResponseFormatter;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import com.thousand_uncles.discord_bot.app.services.MapSessionTrackingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.thousand_uncles.discord_bot.common.util.JSONHandler.processJSON;

@SuppressWarnings("unused")
@Component
public class RabbitEventListener {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    GlobalThings globalThings;

    @Autowired
    MapSessionTrackingService mapSessionTrackingService;

    @Autowired
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

    private static final HashMap<String, String> serverNamesByIP;
    static {
        serverNamesByIP = new HashMap<>();
        serverNamesByIP.put("0.0.0.0:27051", "R.U.N. Uncle Fight Club");
        serverNamesByIP.put("0.0.0.0:27002", "MVM R.U.N. Uncle Fight Club");
        serverNamesByIP.put("0.0.0.0:27133", "PUBLIC R.U.N 1ku (lowpop server)");
        serverNamesByIP.put("0.0.0.0:27043", "Private R.U.N. 1v1KU 1");
        serverNamesByIP.put("0.0.0.0:27035", "Private R.U.N. 1v1KU 2");
        serverNamesByIP.put("0.0.0.0:27316", "Private R.U.N. 1v1KU 3");
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
        long eventTick = eventNode.get("tick").asLong();
        ObjectNode eventData = (ObjectNode) eventNode.get("event_data");
        String serverName = serverNamesByIP.get(eventNode.get("srv").asText());

        switch (eventName){

            case "OnClientPutInServer":
                onClientPutInServerHandle(serverName, eventData);
                break;
            case "player_score_changed":
                onPlayerScoreChangedHandle(serverName, eventData);
                break;
            case "OnClientDisconnect_Post":
                onClientDisconnectPostHandler(serverName, eventData);
                break;
            case "OnMapStart":
                onMapStartHandle(serverName, eventData);
                break;
            case "teamplay_restart_round":
                onTeamplayRestartRoundHandle(serverName);
                break;
            case "TF2_OnWaitingForPlayersEnd":
                onWaitingForPlayersEndHandle(serverName);
                break;
            case "teamplay_setup_finished":
                onTeamplaySetupFinishedHandle(serverName, eventTick);
                break;
            case "teamplay_round_win":
                onTeamplayRoundWinHandle(serverName, eventTick, eventData);
                break;
            case "game_end":
                onGameEndHandle(serverName);
                break;
        }
    }

    private void onPlayerScoreChangedHandle(String serverName, ObjectNode eventData){
        int clientID = eventData.get("player").asInt();
        int delta = eventData.get("delta").asInt();

        mapSessionTrackingService.handlePlayerScoreUpdated(serverName, clientID, delta);
    }

    private void onClientPutInServerHandle(String serverName, ObjectNode eventData){
        boolean isBot = eventData.get("bot").asBoolean();
        if (isBot){
            return;
        }
        int clientID = eventData.get("client").asInt();
        String playerAuth = eventData.get("auth").asText();
        mapSessionTrackingService.handlePlayerJoin(serverName, playerAuth, clientID);
    }

    private void onClientDisconnectPostHandler(String serverName, ObjectNode eventData){
        int clientID = eventData.get("client").asInt();
        mapSessionTrackingService.handlePlayerLeave(serverName, clientID);
    }

    private void onMapStartHandle(String serverName, JsonNode eventData){
        String mapName = eventData.get("map").asText();
        isCertainMap(mapName, serverName);
        serversMaps.put(serverName, mapName);
        mapSessionTrackingService.handleMapStart(mapName, serverName);
    }

    private void onTeamplayRestartRoundHandle(String serverName){
        mapSessionTrackingService.restartSession(serverName);
    }

    private void onWaitingForPlayersEndHandle(String serverName){
        String serverID = serverIDs.get(serverName);
        mapSessionTrackingService.chatMapRecordMessage(serverName, serverID);
        rabbitActionsService.sendToCommand(serverID, "PrintCenterTextAll", verdicts.get(serverID));
        verdicts.remove(serverID);
    }

    private void onTeamplaySetupFinishedHandle(String serverName, long eventTick){
        mapSessionTrackingService.handleStageStart(eventTick, serverName);
    }

    private void onTeamplayRoundWinHandle(String serverName, long eventTick, ObjectNode eventData){
        if (eventData.get("team").asInt() != 3){
            mapSessionTrackingService.dropSession(serverName);
            AppNotifications.RUNserver.RUN_EVENT_INFO("Ghosts are playing on " + serverName + " session dropped.");
            return;
        }
        BigDecimal stageTime;
        try{
            stageTime = BigDecimal.valueOf(eventData.get("round_time").asDouble()).subtract(BigDecimal.valueOf(60));
        } catch (Exception e) {
            mapSessionTrackingService.dropSession(serverName);
            AppNotifications.RUNserver.RUN_EVENT_ERROR("Round finished with broken JSON. Can't get round time. Dropped");
            return;
        }


        mapSessionTrackingService.handleStageWin(eventTick, serverName);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Round finished on " + serverName);
    }

    private void onGameEndHandle(String serverName){
        String serverID = serverIDs.get(serverName);
        try{
            mapSessionTrackingService.handleGameEnd(serverID, serverName);
        } catch (Exception e) {
            return;
        }
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
            chatMessage = "Map doesn't exist in maplist. Tracking will retry on the next map.";
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


