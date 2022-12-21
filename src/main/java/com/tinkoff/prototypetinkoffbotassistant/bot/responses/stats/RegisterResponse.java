package com.tinkoff.prototypetinkoffbotassistant.bot.responses.stats;

import com.tinkoff.prototypetinkoffbotassistant.bot.BotState;
import com.tinkoff.prototypetinkoffbotassistant.bot.responses.ResponseAbstractFactory;
import com.tinkoff.prototypetinkoffbotassistant.model.UserMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class RegisterResponse implements ResponseAbstractFactory {

    @Override
    public SendMessage handle(UserMessage message) {
        return new SendMessage(Long.toString(message.getChatId()), "Register Handler");
    }

    @Override
    public BotState getFactoryName() {
        return BotState.REGISTER;
    }

}
