package com.thousand_uncles.discord_bot.bot.commands;

import com.thousand_uncles.discord_bot.bot.util.YAMLHandler;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;

@Component
@SuppressWarnings("unused")
public class TeachCommand implements SlashCommand{
    @Override
    public String getName() {
        return "teach";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String phrase = event.getOption("phrase")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).flatMap(String::describeConstable).orElseThrow();


        Map<String, ArrayList<String>> dictionary = YAMLHandler.yamlDictionaryRead("resources/dictionary.yml");
        ArrayList <String> words = dictionary.get("words");
        int wordCount = words.size();
        words.add(phrase);
        dictionary.put("words", words);

        YAMLHandler.yamlDictionaryWrite("resources/dictionary.yml", dictionary);
        return event.reply()
                .withEphemeral(false)
                .withContent("Added  \"" + phrase + "\" to dictionary. Total: " + wordCount);
    }
}
