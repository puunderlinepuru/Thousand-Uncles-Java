package com.thousand_uncles.discord_bot.bot.fun_stuff;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;

import java.util.List;

public class Magic_8_ball {
    static List<String> magic8Ball = List.of(
//          Positive [11]
            "It is certain.",
            "https://tenor.com/view/pyro-tf2-pyro-team-fortress-2-tf2-yippee-gif-11048667284588376403",
            "Without a doubt.",
            "Yes – definitely.",
            "Most likely.",
            "Probably yeah..",
            "Yes.",
            "Yes.. I think?",
            "||Do it||",
            "....Sure.",
            "I don't got a PhD in whatever question you're asking me. So yes",

//          Vagueposting [5]
            "Are you stupid?",
            "WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP WAKE UP",
            "Ummmm 42....Yeah. 42",
            "ARE YOU THREATENING ME??!?",
            "https://klipy.com/gifs/invincible-omniman-4",

//          Negative [11]
            "Don’t count on it.",
            "Are you asking me for permission to do something stupid. The answer is No",
            "My reply is no.",
            "My sources say no.",
            "Nuh uh",
            "https://tenor.com/view/nope-videogame-construction-gif-4960956",
            "https://tenor.com/view/sus-scout-lachen-tf2-gif-17981608274864336621",
            "||Nope.||",
            "Just don't",
            "Very doubtful.",
            "No."
    );
    static final int magic8BallSize = 27;

    public static String getAnswers() {
        int n = GlobalThings.rand.nextInt(magic8BallSize);
        return magic8Ball.get(n);
    }
}
