package com.thousand_uncles.discord_bot.bot.util;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class Config {

    Config(){

    }

    private String server_id;
    private String meme_channel_id;
    private String currently_gaming_channel_id;
    private String region_role_message_id;
    private String role_on_join_id;
    private String na_role_id;
    private String eu_role_id;
    private String au_role_id;
    private String asia_role_id;

    private String ready_message;
    private List<String> users_to_timeout;

    private final Available_Categories available_categories = new Available_Categories();

    public String getServer_id() {
        return server_id;
    }
    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getMeme_channel_id() {
        return meme_channel_id;
    }
    public void setMeme_channel_id(String meme_channel_id) {
        this.meme_channel_id = meme_channel_id;
    }

    public String getCurrently_gaming_channel_id() {
        return currently_gaming_channel_id;
    }
    public void setCurrently_gaming_channel_id(String currently_gaming_channel_id) {
        this.currently_gaming_channel_id = currently_gaming_channel_id;
    }

    public String getRegion_role_message_id() {
        return region_role_message_id;
    }
    public void setRegion_role_message_id(String region_role_message_id) {
        this.region_role_message_id = region_role_message_id;
    }

    public String getRole_on_join_id() {
        return role_on_join_id;
    }
    public void setRole_on_join_id(String role_on_join_id) {
        this.role_on_join_id = role_on_join_id;
    }

    public String getNa_role_id() {
        return na_role_id;
    }
    public void setNa_role_id(String na_role_id) {
        this.na_role_id = na_role_id;
    }

    public String getEu_role_id() {
        return eu_role_id;
    }
    public void setEu_role_id(String eu_role_id) {
        this.eu_role_id = eu_role_id;
    }

    public String getAu_role_id() {
        return au_role_id;
    }
    public void setAu_role_id(String au_role_id) {
        this.au_role_id = au_role_id;
    }

    public String getAsia_role_id() {
        return asia_role_id;
    }
    public void setAsia_role_id(String asia_role_id) {
        this.asia_role_id = asia_role_id;
    }

    public String getReady_message() {
        return ready_message;
    }
    public void setReady_message(String ready_message) {
        this.ready_message = ready_message;
    }

    public List<String> getUsers_to_timeout() {
        return users_to_timeout;
    }
    public void setUsers_to_timeout(List<String> users_to_timeout) {
        this.users_to_timeout = users_to_timeout;
    }

    public static class Available_Categories{

        private List<String> check;
        private List<String> update;

        public List<String> getCheck() {
            return check;
        }
        public void setCheck(List<String> check) {
            this.check = check;
        }

        public List<String> getUpdate() {
            return update;
        }
        public void setUpdate(List<String> update) {
            this.update = update;
        }
    }

    public Available_Categories getAvailable_categories() {
        return available_categories;
    }
}
