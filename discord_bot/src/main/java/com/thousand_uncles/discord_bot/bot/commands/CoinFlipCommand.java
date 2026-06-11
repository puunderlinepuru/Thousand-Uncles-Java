package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.GlobalThings;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class CoinFlipCommand implements SlashCommand {

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        String response;

        String reason = event.getOption("reason")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("something");

        String heads = event.getOption("heads")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        String tails = event.getOption("tails")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse(null);

        short rolled = (short) GlobalThings.getRand().nextInt(102);

        if (rolled > 50) {
//            heads
            if (heads != null) {
                response = "Rolled **HEADS!** " + heads +
                        "\n Roll for " + reason;
            } else {
                response = "Rolled **HEADS!** " +
                        "\n Roll for " + reason;
            }


        } else if (rolled < 50) {
//            tails
            if (heads != null) {
                response = "Rolled **TAILS!** " + tails + "\n" +
                        "\n Roll for " + reason;
            } else {
                response = "Rolled **TAILS!** " + "\n" +
                        "\n Roll for " + reason;
            }
        } else {
//            brokeh
            response = "||ȉ̴̬̣̯͒̾͊̍̋̋̂̽̒͗͗̾̅̍̔͝͝m̸̹̠̘̻͚̱̬̫̞̠̤̪̗͍̂̓́̈̉̎̈̾ ̸̧̨̧̢̼̲̬̤͖̭̺̩͍̗̻͇̻̙͔̭͑̏͂̇͜s̴̛̖̖͒̅̔̓͂̋̎̾̈ȏ̴̝̙̺͙̰̭̅͊́́͗̽̀͋̈́́̅̔̒̂̑͐͘̚͝͝ͅr̸͎͔̩̭͇͔͚̜̰͙͎̮͒͆͠r̶̛̘̖̩͙̲̂̌̒̑̇̄́̂͋̍̓͑̋̈̈́̂͊͘͝͝y̵̢͎̮͓̜̮̳͈̤̲͓͙̖͎̙̘͂̀̏͛̿̿͝,̷̧̧̛̬̺̥͚̳̲̤̙͋̓͆̍̕ ̶̧̧̙̳̞̱͈͉͓̩͎̜̝̜͉͆͘t̷̡̨̻̬̯̫̱̦̞̠̰̎r̵̡̨̨͔̫̪͚̪͇͔̼̠̩̪͕̰̤͚̯̙̳͎͓͆́̋̀͑̍̃̌̈́͜y̶̧̧̛̝͍͙̥̱̜͛̌͒́͊̀̽̋̃̔̔̎̈́͑̚͝͝͠ ̵̢͉͔͍͓̙̜͖͍̹̦͓͈̟̰̟̥̯̹̫͖͙́̀̏̀͌̐͐̚͜a̶̲̳̤͚̹̣̰͊͗̐̃̇͌̒̈́͊̆͌̋̈́̇̇͒͊͋̽͘͘͘̕g̴̟̭̟͕̘̤̘͓̤̘̮̬̺̎̊̎͐ͅà̷̧̢̨̛̪̝͍̱̬͚̰̜̣͕̍̈́͗͂͛̿̓̎́̒̃̿͒̄̇̾̀̍̌̚̚͝͝į̸̢̝͉̠̺̹̲̹͓̠͚̱͙̫͇̭̟̈̏͆̑͝n̸͇͍̗̲̑̇̋͊̈́̓̃͝.̵̛̛̘̱̓̆̏͂̎͊̆̏̃̓̎̒̿͐̆̚͘͠ͅ||";
        }


        return event.reply()
                .withEphemeral(false)
                .withContent(response);
    }
}