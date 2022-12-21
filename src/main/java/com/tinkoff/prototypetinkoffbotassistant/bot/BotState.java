package com.tinkoff.prototypetinkoffbotassistant.bot;

public enum BotState {
    START,
    REGISTER,
    HELP,
    DATA_COLLECTING,
    REPORT_COLLECTING;

    /**
     * @param chatType chat from which message was received
     * @param text input message
     * @return Current state of the bot (Or may be called message type)
     */
    public static BotState getBotState(ChatType chatType, String text) {
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
