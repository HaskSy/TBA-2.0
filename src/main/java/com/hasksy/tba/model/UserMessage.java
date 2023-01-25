package com.hasksy.tba.model;

import lombok.Getter;

@Getter
public class UserMessage {

    private final String text;
    private final String username;
    private final long userId;
    private final long chatId;
    private final long timestampMillis;

    public UserMessage(String text, String username, long userId, long chatId, int timestampSeconds) {
        this.text = text.replaceAll("\\s+", " ");
        this.username = username;
        this.userId = userId;
        this.chatId = chatId;
        this.timestampMillis = (long)timestampSeconds*1000;
    }

}
