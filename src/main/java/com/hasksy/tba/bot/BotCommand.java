package com.hasksy.tba.bot;

import com.hasksy.tba.app.model.groupchat.ChatType;

/**
 * This enum contains all possible commands which bot can handle
 */
public enum BotCommand {
    START("/start"),
    REGISTER("/reg"),
    HELP("/help"),
    DATA_COLLECTING("/send"),
    REPORT_COLLECTING;

    private final String prefix;

    /**
     * @param prefix command triggers (prefix with which message from user should start)
     * @implNote if prefix is null, then specific sanitizing conditions should be met to prevent bot from triggering on every message in chat
     */
    BotCommand(String prefix) {
        this.prefix = prefix;
    }

    BotCommand() {
        this.prefix = null;
    }

    public String getPrefix() {
        return prefix;
    }

    /**
     * @param chatType chat from which message was received
     * @param text input message
     * @return Current received command (Message type)
     */
    public static BotCommand getBotCommand(ChatType chatType, String text) {
        switch (chatType) {
            case STATS:
                if (text.startsWith("/send")) {
                    return DATA_COLLECTING;
                }
                if (text.startsWith("/reg")) {
                    return REGISTER;
                }
                if (text.equals("/help")) {
                    return HELP;
                }
            case REPORT:
                return REPORT_COLLECTING;
            case FORBIDDEN:
                // Messages from unwilling chats will be sanitized
                break;
        }
        return START;
    }

}
