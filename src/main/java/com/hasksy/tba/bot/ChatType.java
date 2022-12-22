package com.hasksy.tba.bot;

public enum ChatType {
    STATS,
    REPORT,
    FORBIDDEN;

    /**
     * Defines chat type and marks for sanitizing
     * @param chatId .
     * @return chat type
     */
    public static ChatType getChatType(long chatId) {
        return ChatType.STATS;
    }
}
