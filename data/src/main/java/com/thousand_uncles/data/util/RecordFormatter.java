package com.thousand_uncles.data.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class RecordFormatter {

    @SuppressWarnings("unused")
    public static BigDecimal StringToBigDecimal(String timeString) throws NumberFormatException {
        BigDecimal timeBigDecimal;
        String[] timeStringParts = timeString.split("[:.]");
        int length = timeStringParts.length;
        if (length != 3 && length != 2){
            throw new NumberFormatException("Invalid number of timestamp sections");
        }
        short minutes = Short.parseShort(timeStringParts[0]);
        short seconds = Short.parseShort(timeStringParts[1]);
        int timeShort = (seconds + minutes*60);
        BigDecimal bdSeconds = BigDecimal.ZERO;
        if (length == 3){
            short milliseconds = (Short.parseShort(timeStringParts[2]+ "0"));
            timeBigDecimal = new BigDecimal(milliseconds);
            bdSeconds = timeBigDecimal.divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
        }
        timeBigDecimal = bdSeconds.add(BigDecimal.valueOf(timeShort));
        return timeBigDecimal;
    }

    @SuppressWarnings("unused")
    public static String BigDecimalToString(BigDecimal timeNumber) {
        String timeString;
        BigDecimal result[] = timeNumber.divideAndRemainder(BigDecimal.valueOf(60));
        int minutes = result[0].intValue();
        int seconds = result[1].intValue();
        BigDecimal milliseconds = timeNumber.remainder(BigDecimal.ONE);
        if (seconds < 10){
            timeString = minutes + ":0" + seconds;
        } else {
            timeString = minutes + ":" + seconds;
        }

        if (milliseconds == BigDecimal.ZERO){
            timeString += ".99";
        } else {
            timeString += milliseconds.toPlainString().substring(1);
        }
        return timeString;
    }
}
