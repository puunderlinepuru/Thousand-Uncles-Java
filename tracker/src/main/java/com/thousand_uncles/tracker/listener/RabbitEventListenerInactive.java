package com.thousand_uncles.tracker.listener;

import com.thousand_uncles.tracker.util.AppNotifications;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
@Profile("inactive")
public class RabbitEventListenerInactive implements RabbitEventListener {
    RabbitEventListenerInactive(){
        System.out.println("Using the dud RabbitEventListener");
    }
    public static void setVerdict(String serverName, String message){
        AppNotifications.RUNserver.RUN_EVENT_INFO(serverName + " - verdict set to: \n" + message);
    }
}
