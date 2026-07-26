package com.thousand_uncles.discord_bot.util;

import java.math.BigDecimal;

public class MapSession {
    private String serverName;
    private String mapName;
    private byte stage;
    private short artificialSetupTicks = 0;
    private long startTick;
//    Koth = 30 sec setup
//    TC = 30 sec setup
//    CTF = 60 sec setup
//    weird cp(glassworkd) = 60 sec setup
    private BigDecimal stage_1_time = BigDecimal.ZERO;
    private BigDecimal stage_2_time = BigDecimal.ZERO;
    private BigDecimal stage_3_time = BigDecimal.ZERO;
    private BigDecimal finalTime;

    public MapSession(String serverName) {this.serverName = serverName;}

    public String getServerName() {return serverName;}
    public void setServerName(String serverName) {this.serverName = serverName;}

    public String getMapName() {return mapName;}
    public void setMapName(String mapName) {this.mapName = mapName;}

    public byte getStage() {return stage;}
    public void setStage(byte stage) {this.stage = stage;}

    public void setStartTick(long startTick) {this.startTick = startTick;    }

    public long getStartTick() {return startTick;    }

    public BigDecimal getStage_1_time() {return stage_1_time;}
    public void setStage_1_time(BigDecimal stage_1_time) {this.stage_1_time = stage_1_time;}

    public BigDecimal getStage_2_time() {return stage_2_time;}
    public void setStage_2_time(BigDecimal stage_2_time) {this.stage_2_time = stage_2_time;}

    public BigDecimal getStage_3_time() {return stage_3_time;}
    public void setStage_3_time(BigDecimal stage_3_time) {this.stage_3_time = stage_3_time;}

    public BigDecimal getFinalTime() {return finalTime;}
    public void setFinalTime(BigDecimal finalTime) {this.finalTime = finalTime;}

    public short getArtificialSetupTicks() {return artificialSetupTicks;}
    public void setArtificialSetupTicks(short artificialSetupTicks) {this.artificialSetupTicks = artificialSetupTicks;}
}
