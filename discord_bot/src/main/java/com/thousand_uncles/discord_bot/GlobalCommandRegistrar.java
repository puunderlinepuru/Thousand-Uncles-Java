package com.thousand_uncles.discord_bot;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GlobalCommandRegistrar implements ApplicationRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RestClient client;

    //Use the rest client provided by our Bean
    public GlobalCommandRegistrar(RestClient client) {
        this.client = client;
    }

    private void deleteCommand(long applicationId, ApplicationService applicationService, String commandName) throws Exception {
        final long guildId = Long.parseLong("1326570611975258227");
        Map<String, ApplicationCommandData> discordCommands = applicationService
                .getGuildApplicationCommands(applicationId, guildId)
                .collectMap(ApplicationCommandData::name)
                .block();

        if (discordCommands.isEmpty()) {
            discordCommands = applicationService
                    .getGlobalApplicationCommands(applicationId)
                    .collectMap(ApplicationCommandData::name)
                    .block();

            System.out.println(applicationId);
            System.out.println(applicationService.getGuildApplicationCommands(applicationId, guildId).collectMap(ApplicationCommandData::name).block());

            long commandId = Long.parseLong(String.valueOf(discordCommands.get(commandName).id()));

            applicationService
                    .deleteGlobalApplicationCommand(applicationId, commandId)
                    .subscribe();

        } else {
            System.out.println(applicationId);
            System.out.println(applicationService.getGuildApplicationCommands(applicationId, guildId).collectMap(ApplicationCommandData::name).block());

            long commandId = Long.parseLong(String.valueOf(discordCommands.get(commandName).id()));

            applicationService
                    .deleteGlobalApplicationCommand(applicationId, commandId)
                    .subscribe();
        }

        if (discordCommands.isEmpty()) {
            throw new Exception("Neither method for getting commands worked :c");
        }

// Delete it

    }

//    1441765571829174440

    //This method will run only once on each start up and is automatically called with Spring so blocking is okay.
    @Override
    public void run(ApplicationArguments args) throws IOException {
        //Create an ObjectMapper that supported Discord4J classes
        final JacksonResources d4jMapper = JacksonResources.create();

        // Convenience variables for the sake of easier to read code below.
        PathMatchingResourcePatternResolver matcher = new PathMatchingResourcePatternResolver();
        final ApplicationService applicationService = client.getApplicationService();
        final long applicationId = client.getApplicationId().block();

        //Get our commands json from resources as command data
        List<ApplicationCommandRequest> commands = new ArrayList<>();
        for (Resource resource : matcher.getResources("commands/*.json")) {
            ApplicationCommandRequest request = d4jMapper.getObjectMapper()
                    .readValue(resource.getInputStream(), ApplicationCommandRequest.class);
            commands.add(request);
        }

        /* Bulk overwrite commands. This is now idempotent, so it is safe to use this even when only 1 command
        is changed/added/removed
        */

//        try {
//            deleteCommand(applicationId, applicationService, "check");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, commands)
                .doOnNext(ignore -> LOGGER.debug("Successfully registered Global Commands"))
                .doOnError(e -> LOGGER.error("Failed to register global commands", e))
                .subscribe();
    }
}