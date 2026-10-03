package com.thousand_uncles.google_api_handler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    AppConfig(){}
    private Api_Addresses api_addresses;

    public static class Api_Addresses {
        private String discord_bot;
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }

        public String getDiscord_bot() {
            return discord_bot;
        }
        public void setDiscord_bot(String discord_bot) {
            this.discord_bot = discord_bot;
        }
    }

    public Api_Addresses getApi_addresses() {return api_addresses;}

    public void setApi_addresses(Api_Addresses api_addresses) {
        this.api_addresses = api_addresses;
    }
}
