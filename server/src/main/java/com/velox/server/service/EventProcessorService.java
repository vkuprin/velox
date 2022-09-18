package com.velox.server.service;

import com.velox.server.service.events.EmailEvents;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Locale;

@Component
public class EventProcessorService {

    public enum CommandType {
        SEND_VERIFY("send_verify"), SEND_RESTORE_PASS("send_restore_pass");

        private final String type;

        CommandType(String type) {
            this.type = type;
        }

        public static CommandType asEnum(String status) {
            return valueOf(status.toUpperCase(Locale.US));
        }
    }
    @Async
    @EventListener
    void handleRegistration(EmailEvents event){

        System.out.println("Registration event got triggered for customer::  " + CommandType.asEnum(event.getCommandType()) + event.getEmail() + event.getMessage());
    }
}
