package com.thousand_uncles.discord_bot.common.dto;

public class GamerDTO {
    private String player_auth;
    private int client_id;

    GamerDTO(){}
    GamerDTO(String player_auth, int client_id){
        this.player_auth = player_auth;
        this.client_id = client_id;
    }

    public String getPlayer_auth() {
        return player_auth;
    }

    public void setPlayer_auth(String player_auth) {
        this.player_auth = player_auth;
    }

    public int getClientID() {
        return client_id;
    }

    public void setClientID(int clientID) {
        this.client_id = clientID;
    }
}
