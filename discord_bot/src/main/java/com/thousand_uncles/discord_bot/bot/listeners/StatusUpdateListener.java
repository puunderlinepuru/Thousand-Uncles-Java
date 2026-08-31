package com.thousand_uncles.discord_bot.bot.listeners;

import com.thousand_uncles.data.service.MapRecordServiceProd;
import com.thousand_uncles.discord_bot.app.services.RabbitActionsService;
import com.thousand_uncles.discord_bot.common.config.BotConfig;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SuppressWarnings("unused")
@Component
public class StatusUpdateListener {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    RabbitActionsService rabbitActionsService;

    @Autowired
    MapRecordServiceProd mapRecordServiceProd;

    @Autowired
    BotConfig botConfig;

    GatewayDiscordClient client;

    StatusUpdateListener(GatewayDiscordClient client, BotConfig botConfig){
        this.client = client;

        client.on(MessageUpdateEvent.class, this::onMessageUpdate).subscribe();
    }

    public Mono<Void> onMessageUpdate(MessageUpdateEvent event){

        System.out.println(event.toString());

        return Mono.empty();
    }
}


/*MessageCreateEvent{
    message=Message{
        data=MessageData{
            id=1540312393505177681,
            channelId=1436478432618877141,
            guildId=Possible{1326570611975258227},
            author=UserData{
                id=292677873955766272,
                globalName=پیو پیو پیو|,
                username=pupurupu,
                discriminator=0,
                avatar=4a50be2b879ae90f045c7b702ec66a20,
                banner=Possible.absent,
                accentColor=Possible.absent,
                bot=Possible.absent,
                system=Possible.absent,
                mfaEnabled=Possible.absent,
                locale=Possible.absent,
                verified=Possible.absent,
                email=Possible.absent,
                flags=Possible.absent,
                premiumType=Possible.absent,
                publicFlags=Possible{0},
                avatarDecoration=Possible{Optional.empty}
            },
            member=Possible{
                PartialMemberData{
                    nick=Possible{Optional.empty},
                    banner=Possible{Optional.empty},
                    roles=[
                            J@416cbb8e,
                            joinedAt=2025-01-08T15:18:00.465000+00:00,
                            premiumSince=Possible{Optional.empty},
                            deaf=false,
                            mute=false,
                            communicationDisabledUntil=Possible{Optional.empty},
                            avatarDecoration=Possible.absent,
                            flags=0
                }
            },
            content=,
            timestamp=2026-08-21T10:51:32.232000+00:00,
            tts=false,
            mentionEveryone=false,
            mentions=[
                UserWithMemberData{
                    id=292677873955766272,
                    globalName=پیو پیو پیو|,
                    username=pupurupu,
                    discriminator=0,
                    avatar=4a50be2b879ae90f045c7b702ec66a20,
                    banner=Possible.absent,
                    accentColor=Possible.absent,
                    bot=Possible.absent,
                    system=Possible.absent,
                    mfaEnabled=Possible.absent,
                    locale=Possible.absent,
                    verified=Possible.absent,
                    email=Possible.absent,
                    flags=Possible.absent,
                    premiumType=Possible.absent,
                    publicFlags=Possible{0},
                    avatarDecoration=Possible{Optional.empty},
                    member=Possible{
                        PartialMemberData{
                            nick=Possible{Optional.empty},
                            banner=Possible{Optional.empty},
                            roles=[
                                    J@4fab4dd3,
                                    joinedAt=2025-01-08T15:18:00.465000+00:00,
                                    premiumSince=Possible{Optional.empty},
                                    deaf=false,
                                    mute=false,
                                    communicationDisabledUntil=Possible{Optional.empty},
                                    avatarDecoration=Possible.absent,
                                    flags=0
                            ]
                        }
                    }
                }
            ],
            mentionRoles=[],
            mentionChannels=null,
            attachments=[],
            embeds=[
                EmbedData{
                    title=Possible.absent,
                    type=Possible{poll_result},
                    description=Possible.absent,
                    url=Possible.absent,
                    timestamp=Possible.absent,
                    color=Possible.absent,
                    footer=Possible.absent,
                    image=Possible.absent,
                    thumbnail=Possible.absent,
                    video=Possible.absent,
                    provider=Possible.absent,
                    author=Possible.absent,
                    fields=[
                        EmbedFieldData{
                            name=poll_question_text,
                            value=more test,
                            inline=Possible{false}
                        },
                        EmbedFieldData{
                            name=victor_answer_votes,
                            value=1,
                            inline=Possible{false}
                        },
                        EmbedFieldData{
                            name=total_votes,
                            value=1,
                            inline=Possible{false}
                        },
                        EmbedFieldData{
                            name=victor_answer_id,
                            value=1,
                            inline=Possible{false}
                        },
                        EmbedFieldData{
                            name=victor_answer_text,
                            value=1,
                            inline=Possible{false}
                        }
                    ]
                }
            ],
            reactions=null,
            nonce=Possible.absent,
            pinned=false,
            webhookId=Possible.absent,
            type=46,
            activity=Possible.absent,
            application=Possible.absent,
            applicationId=Possible.absent,
            messageReference=Possible{
                MessageReferenceData{
                    type=0,
                    messageId=Possible{1540312334877196329},
                    channelId=Possible{1436478432618877141},
                    guildId=Possible.absent, failIfNotExists=Possible.absent
                }
            },
            messageSnapshots=null,
            flags=Possible{0},
            stickers=null,
            stickerItems=null,
            referencedMessage=Possible.absent,
            interaction=Possible.absent,
            components=[],
            poll=Possible.absent
        }
        },
        guildId=1326570611975258227,
        member=Member{}
        PartialMember{
            data=MemberData{
                nick=Possible{Optional.empty},
                banner=Possible.absent,
                roles=[
                    J@4b94d0fe,
                    joinedAt=2025-01-08T15:18:00.465000+00:00,
                    premiumSince=Possible{Optional.empty},
                    deaf=false,
                    mute=false,
                    communicationDisabledUntil=Possible.absent,
                    avatarDecoration=Possible.absent,
                    flags=0,
                    user=UserData{
                        id=292677873955766272,
                        globalName=پیو پیو پیو|,
                        username=pupurupu,
                        discriminator=0,
                        avatar=4a50be2b879ae90f045c7b702ec66a20,
                        banner=Possible.absent,
                        accentColor=Possible.absent,
                        bot=Possible.absent,
                        system=Possible.absent,
                        mfaEnabled=Possible.absent,
                        locale=Possible.absent,
                        verified=Possible.absent,
                        email=Possible.absent,
                        flags=Possible.absent,
                        premiumType=Possible.absent,
                        publicFlags=Possible{0},
                        avatarDecoration=Possible{Optional.empty}
                    },
                    pending=Possible.absent,
                    permissions=Possible.absent
            },
            guildId=1326570611975258227
        }
        User{
            data=UserData{
                id=292677873955766272,
                globalName=پیو پیو پیو|,
                username=pupurupu,
                discriminator=0,
                avatar=4a50be2b879ae90f045c7b702ec66a20,
                banner=Possible.absent,
                accentColor=Possible.absent,
                bot=Possible.absent,
                system=Possible.absent,
                mfaEnabled=Possible.absent,
                locale=Possible.absent,
                verified=Possible.absent,
                email=Possible.absent,
                flags=Possible.absent,
                premiumType=Possible.absent,
                publicFlags=Possible{0},
                avatarDecoration=Possible{Optional.empty}
            }
        }
    }*/

//MessageUpdateEvent{messageId=1540312334877196329, channelId=1436478432618877141, guildId=1326570611975258227, old=Message{data=MessageData{id=1540312334877196329, channelId=1436478432618877141, guildId=Possible{1326570611975258227}, author=UserData{id=292677873955766272, globalName=پیو پیو پیو|, username=pupurupu, discriminator=0, avatar=4a50be2b879ae90f045c7b702ec66a20, banner=Possible.absent, accentColor=Possible.absent, bot=Possible.absent, system=Possible.absent, mfaEnabled=Possible.absent, locale=Possible.absent, verified=Possible.absent, email=Possible.absent, flags=Possible.absent, premiumType=Possible.absent, publicFlags=Possible{0}, avatarDecoration=Possible{Optional.empty}}, member=Possible{PartialMemberData{nick=Possible{Optional.empty}, banner=Possible{Optional.empty}, roles=[J@2485fc87, joinedAt=2025-01-08T15:18:00.465000+00:00, premiumSince=Possible{Optional.empty}, deaf=false, mute=false, communicationDisabledUntil=Possible{Optional.empty}, avatarDecoration=Possible.absent, flags=0}}, content=, timestamp=2026-08-21T10:51:18.254000+00:00, tts=false, mentionEveryone=false, mentions=[], mentionRoles=[], mentionChannels=null, attachments=[], embeds=[], reactions=null, nonce=Possible{1540312338614190080}, pinned=false, webhookId=Possible.absent, type=0, activity=Possible.absent, application=Possible.absent, applicationId=Possible.absent, messageReference=Possible.absent, messageSnapshots=null, flags=Possible{0}, stickers=null, stickerItems=null, referencedMessage=Possible.absent, interaction=Possible.absent, components=[], poll=Possible{PollData{question=PollMediaObject{text=Possible{more test}, emoji=Possible.absent}, answers=[PollAnswerObject{answerId=Possible{1}, data=PollMediaObject{text=Possible{1}, emoji=Possible.absent}}, PollAnswerObject{answerId=Possible{2}, data=PollMediaObject{text=Possible{2}, emoji=Possible.absent}}], expiry=2026-08-21T11:51:18.252141+00:00, allowMultiselect=false, layoutType=1, results=Possible.absent}}}}, contentChanged=true, currentContent='', embedsChanged=false, currentEmbeds=[]}