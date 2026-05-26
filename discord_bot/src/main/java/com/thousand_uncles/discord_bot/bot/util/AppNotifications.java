package com.thousand_uncles.discord_bot.bot.util;

public class AppNotifications {

    private static final String DB_RecordInfo = "[ DATABASE RECORD INFO ] ";
    private static final String DB_RecordError = "[ DATABASE RECORD ERROR ] ";
    private static final String DB_RecordWarning = "[ DATABASE RECORD WARNING ] ";

    private static final String JSON_RecordInfo = "[ JSON RECORD INFO ] ";
    private static final String JSON_RecordError = "[ JSON RECORD ERROR ]";
    private static final String JSON_RecordWarning = "[ JSON RECORD WARNING ]";

    private static final String DISCORD_InteractionInfo = "[ DISCORD INTERACTIONINFO ] ";

    public static void DB_RECORD_INFO(String message){
        System.out.println(DB_RecordInfo + message);
    }
    public static void DB_RECORD_ERROR(String message){
        System.out.println(DB_RecordError + message);
    }
    public static void DB_RECORD_WARNING(String message){
        System.out.println(JSON_RecordWarning + message);
    }

    public static void JSON_RECORD_INFO(String message){
        System.out.println(JSON_RecordInfo + message);
    }
    public static void JSON_RECORD_ERROR(String message){
        System.out.println(JSON_RecordError + message);
    }
    public static void JSON_RECORD_WARNING(String message){
        System.out.println(JSON_RecordWarning + message);
    }

    public static void DISCORD_INTERACTION_INFO(String message) {
        System.out.println(DISCORD_InteractionInfo + message);
    }
}
