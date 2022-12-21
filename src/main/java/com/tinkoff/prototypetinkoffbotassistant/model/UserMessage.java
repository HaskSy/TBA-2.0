package com.tinkoff.prototypetinkoffbotassistant.model;

import lombok.Getter;

@Getter
public class UserMessage {

    private final String text;
    private final String username;
    private final long userId;
    private final long chatId;
    private final long timestamp;

    public UserMessage(String text, String username, long userId, long chatId, int timestamp) {
        this.text = text.replaceAll("\\s+", " ");
        this.username = username;
        this.userId = userId;
        this.chatId = chatId;
        this.timestamp = (long)timestamp*1000;
    }

}
