package com.thousand_uncles.discord_bot.bot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

@SuppressWarnings("unused")
@Component
public class MiscStuffCommand implements SlashCommand {

    private final File file = new File("resources/meme_suggestions.yaml");

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

        String memeText = event.getOption("meme_text")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("something");

        String funnyGif= event.getOption("funny_gif")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("something");


            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            try{
                Map<String, Object> data = Map.of(memeText, funnyGif);
                FileWriter writer = new FileWriter(file, true);
                yaml.dump(data, writer);
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        return event.reply()
                .withEphemeral(true)
                .withContent("saved!");
    }
}