package com.thousand_uncles.google_api_handler.data.util;

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

    @SuppressWarnings("unused")
    public static String NumberToString (Short timeNumber) {
        String timeString;
        int minutes = timeNumber / 60;
        int seconds = timeNumber % 60;
        if (seconds < 10){
            timeString = minutes + ":0" + seconds;
        } else {
            timeString = minutes + ":" + seconds;
        }
        return timeString;
    }
}
