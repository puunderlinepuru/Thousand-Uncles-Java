package com.thousand_uncles.discord_bot.commands;

import com.thousand_uncles.discord_bot.YamlReader;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import reactor.core.publisher.Mono;

import java.util.HashMap;

public class TeachCommand implements SlashCommand{
    @Override
    public String getName() {
        return "teach";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String phrase = event.getOption("phrase")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();
        HashMap <String, Object> addition = new HashMap<>();
        addition.put("words", phrase);

        YamlReader yamlReader = new YamlReader("resources/dictionary.yml");
        yamlReader.yamlWrite(addition);
        return event.reply()
                .withEphemeral(false)
                .withContent("Pong!");
    }
}
