package com.hasksy.tba.bot.responses;

import com.hasksy.tba.app.ApplicationService;
import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.bot.model.UserMessage;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class RegisterResponse implements ResponseAbstractFactory {

    private final ApplicationService applicationService;
    public RegisterResponse(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    public SendMessage handle(UserMessage message) {

        String textMessage = message.getText();

        String name = textMessage.replaceFirst(getFactoryName().getPrefix() + " ", "");

        applicationService.registerUser(message.getUserId(), name);

        return new SendMessage(Long.toString(message.getChatId()), "Ваше ФИО записано " + EmojiParser.parseToUnicode(":white_check_mark:"));
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.REGISTER;
    }

}
