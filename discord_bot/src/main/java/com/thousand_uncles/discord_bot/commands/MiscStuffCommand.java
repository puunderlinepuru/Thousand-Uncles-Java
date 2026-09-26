package com.thousand_uncles.discord_bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

@SuppressWarnings("unused")
@Component
public class MiscStuffCommand implements SlashCommand {

    private final File file = new File("resources/sins.txt");

    @Override
    public String getName() {
        return "misc_stuff";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        /*String sampleOption = event.getOption("option_name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("something");*/

        String sin = event.getOption("sin")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("something");

        try{
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.newLine();
            bw.write(sin);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return event.reply()
                .withEphemeral(true)
                .withContent("oh wow..");
    }
}