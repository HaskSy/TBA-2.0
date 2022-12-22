package com.hasksy.tba.bot;

import com.hasksy.tba.model.UserMessage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Facade object which delegates to other objects such tasks as:
 * 1. Input sanitizing
 * 2. Message handling
 */
@Component
public class MessageHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageHandler.class);

    private final BotModelController botModelController;

    public MessageHandler(BotModelController botModelController) {
        this.botModelController = botModelController;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage response = null;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                log.info("New message user: {}, user_id: {}, chat_id: {}, text: {}",
                        message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
                response = handleInputMessage(message);
            }
        }
        return response;
    }

    private SendMessage handleInputMessage(@NotNull Message message) {
        UserMessage userMessage = new UserMessage(
                message.getText(),
                message.getFrom().getUserName(),
                message.getFrom().getId(),
                message.getChatId(),
                message.getDate()
        );
        log.info("Start handling input message from user {} with ID: {}", userMessage.getUsername(), userMessage.getUserId());

        ChatType chatType = ChatType.getChatType(userMessage.getChatId());
        log.info("User message type was set to: {}", chatType);

        BotState botState = BotState.getBotState(chatType, userMessage.getText());
        log.info("BotState was set to: {}, case: {}",
                botState, userMessage.getText());

        return botModelController.execute(botState, userMessage);
    }

}
