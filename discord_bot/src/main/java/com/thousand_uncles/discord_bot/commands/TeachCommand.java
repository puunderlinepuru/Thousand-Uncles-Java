package com.thousand_uncles.discord_bot.commands;

import com.thousand_uncles.discord_bot.YamlReader;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;

@Component
public class TeachCommand implements SlashCommand{
    @Override
    public String getName() {
        return "teach";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        YamlReader yamlReader = new YamlReader("resources/dictionary.yml");
        String phrase = event.getOption("phrase")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();


        Map<String, Object> dictionary = yamlReader.yamlRead();
        ArrayList words = (ArrayList) dictionary.get("words");
        words.add(phrase);
        dictionary.put("words", words);

        yamlReader.yamlWrite(dictionary);
        return event.reply()
                .withEphemeral(false)
                .withContent("Added  \"" + phrase + "\" to dictionary");
    }
}
