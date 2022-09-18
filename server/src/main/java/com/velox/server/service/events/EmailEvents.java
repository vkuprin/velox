package com.velox.server.service.events;


public class EmailEvents {

    private String commandType;
    private String email;
    private String message;

    public EmailEvents(String commandType, String email, String message) {
        this.commandType = commandType;
        this.email = email;
        this.message = message;
    }

    public String getCommandType() {
        return commandType;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
