package com.hasksy.tba.bot.responses.stats;

import com.hasksy.tba.bot.BotCommand;
import com.hasksy.tba.bot.responses.ResponseAbstractFactory;
import com.hasksy.tba.model.UserMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class DataCollectingResponse implements ResponseAbstractFactory {

    @Override
    public SendMessage handle(@NotNull UserMessage message) {
        return new SendMessage(Long.toString(message.getChatId()), "Data Collecting Handler");
    }

    @Override
    public BotCommand getFactoryName() {
        return BotCommand.DATA_COLLECTING;
    }

}
