package com.thousand_uncles.google_api_handler.data.util;

public class RecordFormatter {
    public static Short StringToNumber (String timeString){
        short timeShort;
        String[] timeStringParts = timeString.split(":");
        short minutes = Short.parseShort(timeStringParts[0]);
        short seconds = Short.parseShort(timeStringParts[1]);
        timeShort = (short) (seconds + minutes*60);
        return timeShort;
    }
}
