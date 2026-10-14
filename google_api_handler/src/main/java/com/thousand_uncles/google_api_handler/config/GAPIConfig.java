package com.thousand_uncles.google_api_handler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gapi")
public class GAPIConfig {

    GAPIConfig(){}
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
    public void setApi_addresses(Api_Addresses api_addresses) {this.api_addresses = api_addresses;}

    private Sheets sheets;
    public static class Sheets {

        private Ids ids;
        public static class Ids {
            private String production;
            private String test;

            public String getProduction() {
               return production;
            }

            public void setProduction(String production) {
               this.production = production;
            }

            public String getTest() {
               return test;
            }

            public void setTest(String test) {
               this.test = test;
            }
       }
        public Ids getIds() {return ids;}
        public void setIds(Ids ids) {this.ids = ids;}

//            uncletopia_any:
//      begins: 2
//      ends: 68

        private Uncletopia_Any uncletopia_any;
        public static class Uncletopia_Any {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Uncletopia_Any getUncletopia_any() {return uncletopia_any;}
        public void setUncletopia_any(Uncletopia_Any uncletopia_any) {this.uncletopia_any = uncletopia_any;}

        private Run_Any run_any;
        public static class Run_Any {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Run_Any getRun_any() {return run_any;}
        public void setRun_any(Run_Any run_any) {this.run_any = run_any;}

        private Uncletopia_Cheeseless uncletopia_cheeseless;
        public static class Uncletopia_Cheeseless {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Uncletopia_Cheeseless getUncletopia_cheeseless() {return uncletopia_cheeseless;}
        public void setUncletopia_cheeseless(Uncletopia_Cheeseless uncletopia_cheeseless) {this.uncletopia_cheeseless = uncletopia_cheeseless;}

        private Run_Cheeseless run_cheeseless;
        public static class Run_Cheeseless {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Run_Cheeseless getRun_cheeseless() {return run_cheeseless;}
        public void setRun_cheeseless(Run_Cheeseless run_cheeseless) {this.run_cheeseless = run_cheeseless;}

        private Uncletopia_Solo uncletopia_solo;
        public static class Uncletopia_Solo {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Uncletopia_Solo getUncletopia_solo() {return uncletopia_solo;}
        public void setUncletopia_solo(Uncletopia_Solo uncletopia_solo) {this.uncletopia_solo = uncletopia_solo;}

        private Run_Solo run_solo;
        public static class Run_Solo {
            private int begins;
            private int ends;

            public int getBegins() {return begins;}
            public void setBegins(int begins) {this.begins = begins;}

            public int getEnds() {return ends;}
            public void setEnds(int ends) {this.ends = ends;}
        }
        public Run_Solo getRun_solo() {return run_solo;}
        public void setRun_solo(Run_Solo run_solo) {this.run_solo = run_solo;}
    }
    public Sheets getSheets() {return sheets;}
    public void setSheets(Sheets sheets) {this.sheets = sheets;}
}
