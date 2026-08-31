package com.thousand_uncles.discord_bot.common.util;

public class AppNotifications {

    public static class PostgreSQL{
        private static final String PSQL_RecordInfo = "[ POSTGRESQL RECORD INFO ] ";
        private static final String PSQL_RecordError = "[ POSTGRESQL RECORD ERROR ] ";
        private static final String PSQL_RecordWarning = "[ POSTGRESQL RECORD WARNING ] ";

        public static void PSQL_RECORD_INFO(String message){System.out.println(PSQL_RecordInfo + message);}
        public static void PSQL_RECORD_ERROR(String message){System.out.println(PSQL_RecordError + message);}
        public static void PSQL_RECORD_WARNING(String message){System.out.println(PSQL_RecordWarning + message);}
    }

    public static class JSON{
        private static final String JSON_RecordInfo = "[ JSON RECORD INFO ] ";
        private static final String JSON_RecordError = "[ JSON RECORD ERROR ] ";
        private static final String JSON_RecordWarning = "[ JSON RECORD WARNING ] ";

        public static void JSON_RECORD_INFO(String message){System.out.println(JSON_RecordInfo + message);}
        public static void JSON_RECORD_ERROR(String message){System.out.println(JSON_RecordError + message);}
        public static void JSON_RECORD_WARNING(String message){System.out.println(JSON_RecordWarning + message);}
    }

    public static class Discord{
        private static final String DISCORD_InteractionInfo = "[ DISCORD INTERACTION INFO ] ";
        private static final String DISCORD_InteractionError = "[ DISCORD INTERACTION ERROR ] ";
        private static final String DISCORD_InteractionWarning = "[ DISCORD INTERACTION WARNING ] ";

        private static final String DISCORD_EventInfo = "[ DISCORD EVENT INFO ] ";
        private static final String DISCORD_EventError = "[ DISCORD EVENT ERROR ] ";
        private static final String DISCORD_EventWarning = "[ DISCORD EVENT WARNING ] ";

        public static void DISCORD_INTERACTION_INFO(String message) {System.out.println(DISCORD_InteractionInfo + message);}
        public static void DISCORD_INTERACTION_ERROR(String message) {System.out.println(DISCORD_InteractionError + message);}
        public static void DISCORD_INTERACTION_WARNING(String message) {System.out.println(DISCORD_InteractionWarning + message);}

        public static void DISCORD_EVENT_INFO(String message) {System.out.println(DISCORD_EventInfo + message);}
        public static void DISCORD_EVENT_ERROR(String message) {System.out.println(DISCORD_EventError + message);}
        public static void DISCORD_EVENT_WARNING(String message) {System.out.println(DISCORD_EventWarning + message);}
    }

    public static class RabbitMQ{
        private static final String RABBITMQ_ConsumeInfo = "[ RABBITMQ CONSUME INFO ] ";
        private static final String RABBITMQ_ConsumeError = "[ RABBITMQ CONSUME ERROR ] ";
        private static final String RABBITMQ_ConsumeWarning = "[ RABBITMQ CONSUME WARNING ] ";

        private static final String RABBITMQ_PublishInfo = "[ RABBITMQ PUBLISH WARNING ] ";
        private static final String RABBITMQ_PublishError = "[ RABBITMQ PUBLISH WARNING ] ";
        private static final String RABBITMQ_PublishWarning = "[ RABBITMQ PUBLISH WARNING ] ";

        public static void RABBITMQ_CONSUME_INFO(String message) {System.out.println(RABBITMQ_ConsumeInfo + message);}
        public static void RABBITMQ_CONSUME_ERROR(String message) {System.out.println(RABBITMQ_ConsumeError + message);}
        public static void RABBITMQ_CONSUME_WARNING(String message) {System.out.println(RABBITMQ_ConsumeWarning + message);}

        public static void RABBITMQ_PUBLISH_INFO(String message) {System.out.println(RABBITMQ_PublishInfo + message);}
        public static void RABBITMQ_PUBLISH_ERROR(String message) {System.out.println(RABBITMQ_PublishError + message);}
        public static void RABBITMQ_PUBLISH_WARNING(String message) {System.out.println(RABBITMQ_PublishWarning + message);}
    }

    public static class RUNserver{
        private static final String RUN_EventInfo = "[ R.U.N. EVENT INFO ] ";
        private static final String RUN_EventError = "[ R.U.N. EVENT ERROR ] ";
        private static final String RUN_EventWarning = "[ R.U.N. EVENT WARNING ] ";

        public static void RUN_EVENT_INFO(String message) {System.out.println(RUN_EventInfo + message);}
        public static void RUN_EVENT_ERROR(String message) {System.out.println(RUN_EventError + message);}
        public static void RUN_EVENT_WARNING(String message) {System.out.println(RUN_EventWarning + message);}
    }
}
