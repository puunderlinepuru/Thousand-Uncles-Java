package com.thousand_uncles.discord_bot.bot;

import com.thousand_uncles.discord_bot.bot.config.Config;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class BotActions {

    BotActions(GatewayDiscordClient client, Config config){
        final String READY_MESSAGE = config.getReady_message();

        assert client != null;
        if (READY_MESSAGE != null && !READY_MESSAGE.isEmpty()){
            final String SERVER_ID = config.getServer_id();
            final String MEME_CHANNEL_ID = config.getMeme_channel_id();

            Guild server = client.getGuildById(Snowflake.of(SERVER_ID)).block();
            assert server != null;
            MessageChannel channel = (MessageChannel) server.getChannelById(Snowflake.of(MEME_CHANNEL_ID)).block();

            assert channel != null;
            channel.createMessage().withContent(READY_MESSAGE).block();
        }
    }
}
