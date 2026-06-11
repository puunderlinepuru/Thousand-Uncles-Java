package com.thousand_uncles.dashboard.web.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // This method will be called by the scheduled task
    @Scheduled(fixedRate = 60000) // Every 60 seconds (1 minute)
    @Async
    public void sendCurrentTime() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // Send to all connected clients
        messagingTemplate.convertAndSend("/topic/current-time", currentTime);
    }
}

