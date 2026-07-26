package com.thousand_uncles.discord_bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.Container;
import discord4j.core.object.component.Separator;
import discord4j.core.object.component.TextDisplay;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class HelpCommand implements SlashCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        Container helpContainer = Container.of(
                TextDisplay.of("# Commands"),
                TextDisplay.of("### /achievements " + "\n" +
                        "- link to list of achievements to 1KU composed by iKouRyuu."),
                TextDisplay.of("### /check " + "\n" +
                        "- check the current WR time for map by category."),
                TextDisplay.of("### /coinflip " + "\n" +
                        "- flip a coin for something."),
                TextDisplay.of("### /gamba " + "\n" +
                        "- unnecessarily complicated European Roulette made for fun. No debt mechanic, dw."),
                TextDisplay.of("### /help " + "\n" +
                        "- this command."),
                TextDisplay.of("### /misc_stuff " + "\n" +
                        "- command for random things. Check it's description to see the current ongoing gig."),
                TextDisplay.of("### /random_loadout " + "\n" +
                        "- gives you a randomized TF2 loadout to play with."),
                TextDisplay.of("### /teach " + "\n" +
                        "- puts a phrase in rock's dictionary. He will use it randomly."),
                TextDisplay.of("### /update_any " + "\n" +
                        "- update current WR for a map in Any% category. Needs **imgbb.com** links to screenshots of victory screens of all stages for verification."),
                TextDisplay.of("### /update_solo " + "\n" +
                        "- update current WR for a map in Any% category. Should have both **imgbb.com** links to screenshots and YT link to recording of the map."),
                Separator.of(),
                TextDisplay.of("# Other"),
                TextDisplay.of("""
                        - upon being @'d in dedicated **#the-cave** channel pulls a random phrase out of the **dictionary** filled by **/teach**.
                         - if @ message contains "?" returns random Magic 8-Ball answer
                         - if @ message follows structure "@rock [...] number between *number* and *number*" returns a random number within the boundaries (including them).
                         - upon being @'d in **currently-gaming** channel with structure "@rock [...] mute @user [...]" times them out for 30 seconds. No you can't do it to admis
                        \s""")
        );

        return event.reply()
                .withEphemeral(true)
                .withComponents(helpContainer);
    }
}
