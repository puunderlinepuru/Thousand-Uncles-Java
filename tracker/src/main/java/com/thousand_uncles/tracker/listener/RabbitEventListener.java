package com.thousand_uncles.tracker.listener;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface RabbitEventListener {
    public static void setVerdict(String serverName, String message){

    }
}
