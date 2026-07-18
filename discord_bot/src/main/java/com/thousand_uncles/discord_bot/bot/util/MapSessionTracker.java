package com.thousand_uncles.discord_bot.bot.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.thousand_uncles.discord_bot.bot.service.BotActionsService;
import com.thousand_uncles.discord_bot.data.models.MapRecord;
import com.thousand_uncles.discord_bot.data.models.TestRecord;
import com.thousand_uncles.discord_bot.data.service.MapRecordServiceProd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MapSessionTracker {

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    BotActionsService botActionsService;

    private final Map<String, MapSession> activeSessions = new ConcurrentHashMap<>();

    public void handleMapStart(String mapName, String serverName) {
        MapSession session = activeSessions.computeIfAbsent(serverName, MapSession::new);

        // Set map name and start time
        session.setMapName(mapName);
        session.setStage((byte) 1);
        session.setServerName(serverName);

        AppNotifications.RUNserver.RUN_EVENT_INFO("Started session on server " + serverName +
                " with map " + session.getMapName());
    }

    public void handleSetupFinished (long startTick, String serverName) {
        activeSessions.get(serverName).setStartTick(startTick);

        AppNotifications.RUNserver.RUN_EVENT_INFO("Stage on server " + serverName +
                " commenced with tick " + startTick);
    }

    public void handleNextStage(long eventTick, String serverName) {
        MapSession session = activeSessions.get(serverName);
        long stageTickAmount = eventTick - session.getStartTick();
        BigDecimal stageTime = BigDecimal.valueOf(stageTickAmount).multiply(BigDecimal.valueOf(0.015d));
        byte sessionStage = session.getStage();
        switch (sessionStage){
            case 1:
                session.setStage_1_time(stageTime);
                break;
            case 2:
                session.setStage_2_time(stageTime);
                break;
        }
        AppNotifications.RUNserver.RUN_EVENT_INFO("moved to next stage with time: " + stageTime + " and ticks " + stageTickAmount);
        session.setStage((byte) (session.getStage()+1));
    }

    public void handleRoundWin(long eventTick, String serverName) {
        MapSession session = activeSessions.get(serverName);
        if (session != null) {
            long finalTickAmount = eventTick - session.getStartTick();
            BigDecimal finalTime = BigDecimal.valueOf(finalTickAmount).multiply(BigDecimal.valueOf(0.015d));

            BigDecimal firstStageTime = session.getStage_1_time();
            if (firstStageTime == null){
                AppNotifications.RUNserver.RUN_EVENT_INFO("Session ended without stages, total time: " + finalTime);
                checkIfWR(session);
                return;
            }

            finalTime = finalTime.add(firstStageTime);

            BigDecimal secondStageTime = session.getStage_2_time();
            if (secondStageTime == null){
                session.setFinalTime(finalTime);
                AppNotifications.RUNserver.RUN_EVENT_INFO("Session ended on 2nd stage, total time: " + finalTime);
                checkIfWR(session);
                return;
            }

            finalTime = finalTime.add(secondStageTime);

            session.setFinalTime(finalTime);
            AppNotifications.RUNserver.RUN_EVENT_INFO("Session  completed with time " +
                    finalTime + " on server " + serverName);

            checkIfWR(session);

            // Move to completed sessions
            activeSessions.remove(serverName);

            AppNotifications.RUNserver.RUN_EVENT_INFO("Removed session");
        } else {
            AppNotifications.RUNserver.RUN_EVENT_ERROR("Couldn't find existing session on server " + serverName + ". Record is dropped.");
        }
    }

    void checkIfWR(MapSession mapSession){
        String unsuffixedMapName = GlobalThings.getMapSuffixes().get(mapSession.getMapName());
        int mapID;
        if (unsuffixedMapName == null){
            mapID = GlobalThings.getMapIDS().indexOf(mapSession.getMapName());
        } else {
            mapSession.setMapName(unsuffixedMapName);
            mapID = GlobalThings.getMapIDS().indexOf(unsuffixedMapName);
        }

        TestRecord testRecord = mapRecordServiceProd.getTestRecord(mapID);

        if (testRecord == null){
            mapRecordServiceProd.addTestRecord(
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
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Couldn't find record in TEST for " + mapSession.getMapName() + " with ID " + mapID +  ". Adding...");
            botActionsService.sendIntoCave("Couldn't find record for " + mapSession.getMapName() + " on " + mapSession.getServerName() + ". Adding. \n Time: " + DiscordBotResponseFormatter.NumberToString(mapSession.getFinalTime()));
        } else if (testRecord.getCurr_wr_seconds().compareTo(mapSession.getFinalTime()) > 0) {
            mapRecordServiceProd.addTestRecord(
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
            botActionsService.sendIntoCave("WR beaten for " + mapSession.getMapName() + " on " + mapSession.getServerName() + ". Updating... \n Run time: " + DiscordBotResponseFormatter.NumberToString(mapSession.getFinalTime()));
        } else {
            AppNotifications.PostgreSQL.PSQL_RECORD_INFO("Record in TEST for " + mapSession.getMapName() + " with ID " + mapID + ". Is better. ignoring");
        }
    }
}
