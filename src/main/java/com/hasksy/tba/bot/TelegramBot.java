package com.hasksy.tba.bot;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * {@link TelegramWebhookBot} abstract class implementation from TelegramBot library
 */
@Setter
@Getter
public class TelegramBot extends TelegramWebhookBot {

    private String botPath;
    private String botUsername;
    private String botToken;

    private final MessageHandler messageHandler;

    public TelegramBot(DefaultBotOptions botOptions, MessageHandler messageHandler) {
        super(botOptions);
        this.messageHandler = messageHandler;
    }

    /**
     * Implementing callback function on received input messages
     * @param update Update received
     */
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return messageHandler.handleUpdate(update);
    }

}
