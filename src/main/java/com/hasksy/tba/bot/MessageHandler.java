package com.hasksy.tba.bot;

import com.hasksy.tba.app.ApplicationService;
import com.hasksy.tba.app.model.groupchat.ChatType;
import com.hasksy.tba.bot.model.UserMessage;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MessageHandler {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(MessageHandler.class);

    private final BotModelController botModelController;
    private final ApplicationService applicationService;

    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage response = null;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                UserMessage userMessage = new UserMessage(
                        message.getText(),
                        message.getFrom().getUserName(),
                        message.getFrom().getId(),
                        message.getChatId(),
                        message.getDate()
                );
                log.info("[Message timestamp: {}] New message user: {}, user_id: {}, chat_id: {}, text: {}",
                        userMessage.getTimestampMillis(), userMessage.getUsername(), userMessage.getUserId(), userMessage.getChatId(), userMessage.getText());
                response = handleInputMessage(userMessage);
            }
        }
        return response;
    }

    private SendMessage handleInputMessage(@NotNull UserMessage message) {
        log.info("Start handling input message from user {} with ID: {}", message.getUsername(), message.getUserId());

        ChatType chatType = applicationService.getChatType(message.getChatId());
        log.info("User message type was set to: {}", chatType);

        BotCommand botCommand = BotCommand.getBotCommand(chatType, message.getText());
        log.info("BotState was set to: {}, case: {}",
                botCommand, message.getText());

        return botModelController.execute(botCommand, message);
    }

}
