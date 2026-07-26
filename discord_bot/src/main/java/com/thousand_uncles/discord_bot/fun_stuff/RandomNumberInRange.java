package com.thousand_uncles.discord_bot.fun_stuff;

import com.thousand_uncles.discord_bot.util.GlobalThings;

public class RandomNumberInRange {
    public static Integer getNumber(int firstNumber, int secondNumber){
        int n = GlobalThings.getRand().nextInt(secondNumber + 1 - firstNumber) + firstNumber;
        System.out.println("from" + firstNumber + " to " + secondNumber + " rolled " + n);
        return n;
    }
}
