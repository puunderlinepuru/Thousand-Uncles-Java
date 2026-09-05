package com.thousand_uncles.discord_bot.app.services;

import com.thousand_uncles.data.models.run.RunAnyPercentMapRecordEntry;
import com.thousand_uncles.data.models.run.RunCheeselessMapRecordEntry;
import com.thousand_uncles.data.models.run.RunSoloMapRecordEntry;
import com.thousand_uncles.data.models.run.TestRecord;
import com.thousand_uncles.data.models.uncletopia.AnyPercentMapRecordEntry;
import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.app.listener.RabbitEventListener;
import com.thousand_uncles.discord_bot.bot.services.BotActionsService;
import com.thousand_uncles.discord_bot.bot.util.DiscordBotResponseFormatter;
import com.thousand_uncles.discord_bot.common.dto.MapSession;
import com.thousand_uncles.discord_bot.common.util.AppNotifications;
import com.thousand_uncles.discord_bot.common.util.GlobalThings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapSessionTrackingService {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    BotActionsService botActionsService;

    @Autowired
    RabbitActionsService rabbitActionsService;

    private final Map<String, MapSession> activeSessions = new ConcurrentHashMap<>();

    public void handleMapStart(String mapName, String serverName){
        MapSession session = activeSessions.computeIfAbsent(serverName, MapSession::new);

        session.setMapName(mapName);
        session.setServerName(serverName);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Started map: " + mapName + " on server " + serverName);
    }

    public void handleStageStart(long eventTick, String serverName){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }
        session.setStartTick(eventTick);

        AppNotifications.RUNserver.RUN_EVENT_INFO("Stage started on server " + serverName + " with tick " + eventTick);
    }

    public void handlePlayerJoin(String serverName, String playerAuth, int clientID){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        Map<Integer, String> currentlyPlaying = session.getCurrentlyPlaying();

        currentlyPlaying.put(clientID, playerAuth);
        session.setCurrentlyPlaying(currentlyPlaying);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Added client " + clientID + " with Auth " + playerAuth + " to currently playing. Total playing: " + currentlyPlaying.size() + ". \n Players: " + currentlyPlaying);

    }

    public void handlePlayerScoreUpdated(String serverName, int clientID, int delta){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        Map<Integer, String> currentlyPlaying = session.getCurrentlyPlaying();
        Map<String, Integer> playersScore = session.getPlayersScore();
        String playerAuth = currentlyPlaying.get(clientID);
        if (playerAuth == null){
            AppNotifications.RUNserver.RUN_EVENT_INFO("client ID " + clientID + " doesn't correspond to any active players");
            return;
        }
        playersScore.merge(playerAuth, delta, Integer::sum);
        session.setPlayersScore(playersScore);
    }

    public void handlePlayerLeave(String serverName, int clientID){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        Map<Integer, String> currentlyPlaying = session.getCurrentlyPlaying();

        currentlyPlaying.remove(clientID);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Removed player " + clientID + " from currently gaming");
    }

    public void handleStageWin(long eventTick, String serverName) {
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        long startTick = session.getStartTick();
        AppNotifications.RUNserver.RUN_EVENT_INFO("Round ended. Got starting tick: " + startTick);
        long totaStageTicks = eventTick - startTick;
        BigDecimal stageTime = (BigDecimal.valueOf(totaStageTicks)).multiply(BigDecimal.valueOf(0.015d));

        byte sessionStage = session.getStage();
        switch (sessionStage){
            case 1:
                session.setStage_1_time(stageTime);
                break;
            case 2:
                session.setStage_2_time(stageTime);
                break;
            case 3:
                session.setStage_3_time(stageTime);
                break;
        }

        AppNotifications.RUNserver.RUN_EVENT_INFO("Stage " + sessionStage + "->" + (sessionStage + 1) + ". Total ticks:" + totaStageTicks + ". Stage time: " + stageTime + ". Human format: " + DiscordBotResponseFormatter.NumberToString(stageTime));
        session.setStage((byte) (sessionStage + 1));
    }

    public void handleGameEnd(String serverID, String serverName) {
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        BigDecimal finalTime = session.getStage_1_time();
        BigDecimal stage1Time = session.getStage_1_time();
        System.out.println("Stage 1 time: " + stage1Time);
        BigDecimal stage2Time = session.getStage_2_time();
        System.out.println("Stage 2 time: " + stage2Time);
        BigDecimal stage3Time = session.getStage_3_time();
        System.out.println("Stage 3 time: " + stage3Time);

        finalTime = finalTime.add(stage2Time);
        finalTime = finalTime.add(stage3Time);
        session.setFinalTime(finalTime);

        AppNotifications.RUNserver.RUN_EVENT_INFO("Game ended with time: " + finalTime + ".");
        String timeHumanFormat = DiscordBotResponseFormatter.NumberToString(finalTime);

        byte recordCategories = 0;

        System.out.println("checking if Any% WR");
        recordCategories += checkIfAnyPercentWR(session);
        System.out.println("checking if Solo%WR");
        recordCategories += checkIfSoloWR(session);
        System.out.println("checking if Cheeseless% WR");
        recordCategories += checkIfCheeselessWR(session);

        System.out.println("recordCategories: " + recordCategories);
        String message;
        switch (recordCategories){
            case 0:
                message = "No records were broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
            case 1:
                message = "Any% record was broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
//                botActionsService.sendIntoCave("Any% record was broken on " + session.getMapName() + "|" + serverName + "\n" +
//                        "new WR: " + timeHumanFormat);
                break;
            case 2:
                message = "Solo% record was broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
            case 4:
                message = "Cheeseless% record was broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;

            case 3:
                message = "Any% and Solo% records were broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
            case 5:
                message = "Any% and Cheeseless% records were broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
            case 6:
                message = "Solo% and Cheeseless% records were broken";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
            case 7:
                message = "WRs for all categories were broken!";
                AppNotifications.RUNserver.RUN_EVENT_INFO(message);
                RabbitEventListener.setVerdict(serverName, message);
                break;
        }

        AppNotifications.RUNserver.RUN_EVENT_INFO("Session on " + serverName+ " ended with time" + session.getFinalTime());

//        activeSessions.remove(serverName);
        restartSession(serverName);
        String chatMessage = "Your time: " + timeHumanFormat;
        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
    }

    public void restartSession(String serverName){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        session.setStage((byte) 1);

        session.setStage_1_time(BigDecimal.ZERO);
        session.setStage_2_time(BigDecimal.ZERO);
        session.setStage_3_time(BigDecimal.ZERO);

        session.setPlayersScore(HashMap.newHashMap(40));
        AppNotifications.RUNserver.RUN_EVENT_INFO("Session restarted on server " + serverName + ". Reset stage to 1, zeroed all times, reset score.");
    }

    public void dropSession(String serverName){
        MapSession session = activeSessions.get(serverName);
        if (session == null) {
            AppNotifications.RUNserver.RUN_EVENT_ERROR("Session on server " + serverName + " is already dropped");
            return;
        }

        activeSessions.remove(serverName);
        AppNotifications.RUNserver.RUN_EVENT_INFO("Session on server " + serverName + " dropped");
    }

    byte checkIfAnyPercentWR(MapSession mapSession){
        String unsuffixedMapName = GlobalThings.getMapSuffixes().get(mapSession.getMapName());
        int mapID;
        if (unsuffixedMapName == null){
            mapID = GlobalThings.getMapIDS().indexOf(mapSession.getMapName());
        } else {
            mapSession.setMapName(unsuffixedMapName);
            mapID = GlobalThings.getMapIDS().indexOf(unsuffixedMapName);
        }

        RunAnyPercentMapRecordEntry runAnyPercentMapRecordEntry = (RunAnyPercentMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_any");

        if (runAnyPercentMapRecordEntry == null){
            RunAnyPercentMapRecordEntry savedRecord = (RunAnyPercentMapRecordEntry) mapRecordServiceProd.saveRunAny(
                    mapID,
                    mapSession.getMapName(),
                    mapSession.getFinalTime(),
                    BigDecimal.valueOf(0.0),
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    mapSession.getStage_1_time(),
                    mapSession.getStage_2_time(),
                    mapSession.getStage_3_time()
            );
            System.out.println("savedRecord: " + savedRecord);
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Couldn't find record in Run Any% for " + mapSession.getMapName() + " with ID " + mapID +  ". Adding...");
            return 1;
        } else if (runAnyPercentMapRecordEntry.getCurr_wr_seconds().compareTo(mapSession.getFinalTime()) > 0) {
            mapRecordServiceProd.saveRunAny(
                    mapID,
                    mapSession.getMapName(),
                    mapSession.getFinalTime(),
                    BigDecimal.valueOf(0.0),
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    mapSession.getStage_1_time(),
                    mapSession.getStage_2_time(),
                    mapSession.getStage_3_time()
            );
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in TEST for " + mapSession.getMapName() + " with ID " + mapID + ". Is worse. Updating");
            System.out.println("test 3");
            return 1;
        } else {
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in TEST for " + mapSession.getMapName() + " with ID " + mapID + ". Is better. ignoring");
            System.out.println("test 4");
            return 0;
        }
    }

    byte checkIfSoloWR(MapSession session){
        String unsuffixedMapName = GlobalThings.getMapSuffixes().get(session.getMapName());
        int mapID;
        if (unsuffixedMapName == null){
            mapID = GlobalThings.getMapIDS().indexOf(session.getMapName());
        } else {
            session.setMapName(unsuffixedMapName);
            mapID = GlobalThings.getMapIDS().indexOf(unsuffixedMapName);
        }

        Map<String, Integer> playersScores = session.getPlayersScore();
        int totalScore = 0;
        System.out.println("players scores: ");
        for (Integer score : playersScores.values()){
            System.out.println(score);
            totalScore += score;
        }

        int scoreBarrier = (int) (totalScore * 0.95);
        String scoreBarrierPassedPlayerAuth = null;

        for (Map.Entry<String, Integer> entry : playersScores.entrySet()){
            if (entry.getValue() >= scoreBarrier){
                scoreBarrierPassedPlayerAuth = entry.getKey();
            }
        }
        if (scoreBarrierPassedPlayerAuth == null){
            AppNotifications.RUNserver.RUN_EVENT_INFO("Don't have players with more than 95% of total score");
            return 0;
        }
        AppNotifications.RUNserver.RUN_EVENT_INFO("Player " + scoreBarrierPassedPlayerAuth + " passed solo% condition. Percentage: " + ((playersScores.get(scoreBarrierPassedPlayerAuth) / totalScore) * 100));
        RunSoloMapRecordEntry existingRecord = (RunSoloMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_solo");
        System.out.println("existingRecord: " + existingRecord);
        if (existingRecord == null){
            mapRecordServiceProd.saveRunSolo(
                    mapID,
                    session.getMapName(),
                    session.getFinalTime(),
                    BigDecimal.valueOf(0.0),
                    scoreBarrierPassedPlayerAuth,
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    "added automatically",
                    session.getStage_1_time(),
                    session.getStage_2_time(),
                    session.getStage_3_time()
            );
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Couldn't find record in Solo% for " + session.getMapName() + " with ID " + mapID + ". Updating");
            return 2;
        } else if (existingRecord.getCurr_wr_seconds().compareTo(session.getFinalTime()) > 0){
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in Solo% for " + session.getMapName() + " with ID " + mapID + " is worse. Updating");
            return 2;
        }
        return 0;

    }

    byte checkIfCheeselessWR(MapSession session){
        String unsuffixedMapName = GlobalThings.getMapSuffixes().get(session.getMapName());
        int mapID;
        if (unsuffixedMapName == null){
            mapID = GlobalThings.getMapIDS().indexOf(session.getMapName());
        } else {
            session.setMapName(unsuffixedMapName);
            mapID = GlobalThings.getMapIDS().indexOf(unsuffixedMapName);
        }

        RunCheeselessMapRecordEntry runCheeselessMapRecordEntry = (RunCheeselessMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_cheeseless");

        if (runCheeselessMapRecordEntry == null){
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Couldn't find record in Cheeseless% for " + session.getMapName() + " with ID " + mapID +  ".");
            return 4;
        } else if (runCheeselessMapRecordEntry.getCurr_wr_seconds().compareTo(session.getFinalTime()) > 0) {
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in cheeseless for " + session.getMapName() + " with ID " + mapID + ". Is worse. Could update");
            return 4;
        } else {
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in cheeseless for " + session.getMapName() + " with ID " + mapID + ". Is better. ignoring");
            RabbitEventListener.setVerdict(session.getServerName(), "Record in cheeseless for " + session.getMapName() + " is better. ignoring");
            return 0;
        }
    }
    public void chatMapRecordMessage(String serverName, String serverID){
        MapSession session = activeSessions.get(serverName);
        if (session == null){
            AppNotifications.RUNserver.RUN_EVENT_ERROR(" Session not present on server " + serverName);
            return;
        }

        String mapUnsuffixed = GlobalThings.getMapSuffixes().get(session.getMapName());
        int mapID;
        if (mapUnsuffixed == null){
            mapID = GlobalThings.getMapIDS().indexOf(session.getMapName());
        } else {
            mapID = GlobalThings.getMapIDS().indexOf(mapUnsuffixed);
        }
        String chatMessage;
        if (mapID == -1){
            chatMessage = "Map doesn't exist in maplist. Tracking will retry on the next map.";
            rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
            return;
        }
        RunAnyPercentMapRecordEntry existingAnyRecord = (RunAnyPercentMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_any");
        if (existingAnyRecord == null) {
            chatMessage = "Couldn't find existing RUN Any% record for map. You may be the first!";
//            rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
//            return;
        } else {
            String currWRtime = DiscordBotResponseFormatter.NumberToString(existingAnyRecord.getCurr_wr_seconds());
            chatMessage = "Current RUN Any% WR for map: " + currWRtime;
        }
        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);

        RunSoloMapRecordEntry existingSoloRecord = (RunSoloMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_solo");
        if (existingSoloRecord == null) {
            chatMessage = "Couldn't find existing RUN Solo% record for map. You may be the first!";
//            rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
//            return;
        } else {
            String currWRtime = DiscordBotResponseFormatter.NumberToString(existingSoloRecord.getCurr_wr_seconds());
            chatMessage = "Current RUN Solo% WR for map: " + currWRtime;
        }
        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);

        RunCheeselessMapRecordEntry existingCheeselessRecord = (RunCheeselessMapRecordEntry) mapRecordServiceProd.getRecord(mapID, "run_cheeseless");
        if (existingCheeselessRecord == null) {
            chatMessage = "Couldn't find existing RUN Cheeseless% record for map. You may be the first!";
//            return;
        } else {
            String currWRtime = DiscordBotResponseFormatter.NumberToString(existingCheeselessRecord.getCurr_wr_seconds());
            chatMessage = "Current RUN Cheeseless% WR for map: " + currWRtime;
        }
        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);

//        rabbitActionsService.sendToCommand(serverID, "PrintToChatAll", chatMessage);
    }
}
