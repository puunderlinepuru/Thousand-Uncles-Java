package com.thousand_uncles.discord_bot.fun_stuff;

import java.util.List;
import java.util.Random;

public class Magic_8_ball {
    static List<String> magic8Ball = List.of(
            // Positive
            "It is certain.", "It is decidedly so.", "Without a doubt.", " Yes – definitely.", "You may rely on it.",
            "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", " Signs point to yes.",

            // Neutral
            "Reply hazy, try again.", "Ask again later.", "Better not tell you now.", " Cannot predict now.", "Concentrate and ask again.",

            // Negative
            "Don’t count on it.", "My reply is no.", "My sources say no.", " Outlook not so good.", "Very doubtful.", "No."
    );
    static int magic8BallSize = 21;

    public static String getAnswers() {
        Random rand = new Random();
        int n = rand.nextInt(magic8BallSize);
        return (String) magic8Ball.get(n);
    }
}
