package com.thousand_uncles.discord_bot.data.util;

public class RecordFormatter {
    @SuppressWarnings("unused")
    public static Short StringToNumber (String timeString){
        short timeShort;
        String[] timeStringParts = timeString.split(":");
        short minutes = Short.parseShort(timeStringParts[0]);
        short seconds = Short.parseShort(timeStringParts[1]);
        timeShort = (short) (seconds + minutes*60);
        return timeShort;
    }

    public static String NumberToString (short timeNumber) {
        String timeString;
        int minutes = timeNumber / 60;
        int seconds = timeNumber % 60;
        timeString = minutes + ":" + seconds;
        return timeString;
    }
}
