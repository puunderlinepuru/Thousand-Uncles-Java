package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.common.util.GlobalThings;

import java.util.List;

public class Magic_8_ball {
    static final List<String> magic8Ball = List.of(
//          Positive [8]
            "It is certain.",
            "Without a doubt.",
            "Most likely.",
            "Probably yeah..",
            "Yes.",
            "Yes.. I think?",
            "....Sure.",
            "I don't got a PhD in whatever question you're asking me. So yes",

//          Vagueposting [2]
            "Are you stupid?",
            "https://klipy.com/gifs/invincible-omniman-4",

//          Negative [8]
            "Don’t count on it.",
            "Are you asking me for permission to do something stupid. The answer is No",
            "My reply is no.",
            "My sources say no.",
            "https://tenor.com/view/nope-videogame-construction-gif-4960956",
            "||Nope.||",
            "Very doubtful.",
            "No."
    );
    static final int magic8BallSize = 18;

    public static String getAnswers() {
        int n = GlobalThings.getRand().nextInt(magic8BallSize);
        return magic8Ball.get(n);
    }
}
